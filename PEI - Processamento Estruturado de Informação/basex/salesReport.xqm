module namespace page = 'http://basex.org/examples/web-page';
declare namespace salesModule = 'http://example.com/sales-module';


(: Obter relatório de vendas de um mês :)
declare
 %rest:path("/getSales")
 %rest:query-param("ano", "{$ano}")
 %rest:query-param("mes", "{$mes}")
 %rest:GET
function page:getSales($ano as xs:integer, $mes as xs:integer) {
  let $xml := page:getSalesRawData($ano, $mes)
  let $parsedXml := page:transformSalesXML($xml)
  return $parsedXml
};

declare function page:getSalesRawData($ano as xs:integer, $mes as xs:integer) {
  let $rightMes := fn:format-number($mes, "00") (: com isto não vai ignorar o 0 caso o mes seja 03 :)
  
   let $rightProximoMes :=
    if ($mes eq 12) (: se mes for iguala 12 volta a um:)
    then fn:format-number(1, "00")
    else fn:format-number($mes + 1, "00")
  
  let $nextYear := (: se mes igual a 12 incrementa o ano 1:)
    if ($mes eq 12)
    then $ano + 1
    else $ano  

  let $url := "https://eu-west-2.aws.data.mongodb-api.com/app/data-ntgsq/endpoint/data/v1"
  let $findSuffix := "/action/find"
  let $apiKey := "S1heqodn63sFXzyjgJEUfrRiM985KlqsDQg9JYA8UhBqwSzFB9oW9YtswZcqCYN0"
  let $contentType := "application/json"
  let $body := concat('{
      "collection":"SalesInfo",
      "database":"Projeto",
      "dataSource":"Cluster0",

        "filter": {
            "date": {
                "$gte": {"$date": "', $ano, '-', $rightMes, '-01T00:00:00Z"},
                "$lt": {"$date": "', $nextYear, '-', $rightProximoMes, '-01T00:00:00Z"}
            }
        },
        "projection": {
       "_id": 0,
       "customer": 1,
       "total_sales": 1,
       "date": 1,
       "invoice_id": 1,
       "salesLines": 1,
       "productsSold": 1
   }
}')


  let $httpResponse := http:send-request(
    <http:request method='post'>
      {<http:header name="Access-Control-Request-Headers" value="*"/>},
      {<http:header name="api-key" value='{$apiKey}'/>}
      
      {<http:body media-type='{$contentType}'>{$body}</http:body>}
    </http:request>,
    concat($url, $findSuffix)
  )
  
  return $httpResponse[2]
};

declare function page:getDistinctSalesCount($xml) {
  let $distinctInvoiceIds :=
    for $sale in $xml/json/documents/*
    return distinct-values($sale/invoice__id)

  let $count := count($distinctInvoiceIds)
  return $count
};

declare function page:getTotalSalesValue($xml) {
   let $totalSale :=
    for $sale in $xml/json/documents/*
    return ($sale/total__sales)

  let $totalSales := sum($totalSale)
  return $totalSales
};


declare function page:getDistinctProductCount($xml) {
  let $distinctProductIds :=
    for $sale in $xml/json/documents/*
    return distinct-values($sale/productsSold//id)

  let $count := count($distinctProductIds)
  return $count
};

declare function page:getDistinctCustomerCount($xml) {
  let $distinctCustomerIds :=
    for $sale in $xml/json/documents/*
    return distinct-values($sale/customer//email)

  let $count := count($distinctCustomerIds)
  return $count
};

declare function page:transformSalesXML($xml) {
 <SalesReport>

 <number_of_sales> {page:getDistinctSalesCount($xml)} </number_of_sales>
 
 <total_sales_value> {page:getTotalSalesValue($xml)} </total_sales_value>
 
 <number_of_distinct_products>{page:getDistinctProductCount($xml)}</number_of_distinct_products>
 <number_of_distinct_customers>{page:getDistinctCustomerCount($xml)}</number_of_distinct_customers>
 
    <sales>
    {
      for $sale in $xml/json/documents/*
      return (
     <sale>
      <invoice_id>{$sale/invoice__id/text()}</invoice_id>
      <sale_date>{$sale/date/text()}</sale_date>
      <total_sale>{$sale/total__sales/text()}</total_sale>
      <customer>
        <first_name>{$sale/customer//first__name/text()}</first_name>
        <last_name>{$sale/customer//last__name/text()}</last_name>
        <email>{$sale/customer//email/text()}</email>
        <address>
          <postal_code>{$sale/customer//address//postal__code/text()}</postal_code>
          <city>{$sale/customer//address//city//city/text()}</city>
          <country>{$sale/customer//address//country//country/text()}</country>
        </address>
        <customer_type> {$sale/customer//customerType/text()} </customer_type>
        <customer_purchases> {$sale/customer//purchases//purchase__count/text()} </customer_purchases>
      </customer>
    
    <products>
      {
        for $product in $sale/productsSold
        return
          <product>
            <product_code>{$product//id/text()}</product_code>
            <list_price>{$product//list__price/text()}</list_price>
            <brand>{$product//brand/text()}</brand>
            <model>{$product//model/text()}</model>
            <subcategories>
      {
        for $subcategory in $product//sub__categories//sub__category
        return
          <subcategory>
            <name>{$subcategory//name/text()}</name>
            <category_name>{$subcategory//category//name/text()}</category_name>
          </subcategory>
      }
    </subcategories>
          </product>
      }
    </products>
    
    <sale_lines>
      {
        for $line in $sale/salesLines
        return
          <sale_line>
            <number>{$line//id/text()}</number>
            <product_code>{$line//product_id/text()}</product_code>
            <total_with_vat>{$line//total__with__vat/text()}</total_with_vat>
            <quantity>{$line//quantity/text()}</quantity>
          </sale_line>
      }
    </sale_lines>
  </sale>
      )  
    }
   </sales>
   </SalesReport>
};