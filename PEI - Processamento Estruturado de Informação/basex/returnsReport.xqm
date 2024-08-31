module namespace page = 'http://basex.org/examples/web-page';
declare namespace salesModule = 'http://example.com/sales-module';


(: Obter relatório de vendas de um mês :)
declare
 %rest:path("/getReturns")
 %rest:query-param("ano", "{$ano}")
 %rest:query-param("mes", "{$mes}")
 %rest:GET
function page:getSales($ano as xs:integer, $mes as xs:integer) {
  let $xml := page:getReturnsRawData($ano, $mes)
  let $parsedXml := page:transformRetursXML($xml)
  return $parsedXml
};

declare function page:getReturnsRawData($ano as xs:integer, $mes as xs:integer) {
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
      "collection":"Returns",
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
       "product_id": 1,
       "sale_data": 1,
       "date": 1,
       "invoice_id": 1,
       "early_return": 1,
       "days_to_return": 1,
       "productsReturned": 1
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


declare function page:getDistinctProductCount($xml) {
  let $distinctProductIds :=
    for $return in $xml/json/documents/*
    return distinct-values($return/productsReturned//id)

  let $count := count($distinctProductIds)
  return $count
};



declare function page:transformRetursXML($xml) {
 <ReturnsReport>
 <number_of_distinct_products>{page:getDistinctProductCount($xml)}</number_of_distinct_products>
    <returns>
    {
      for $return in $xml/json/documents/*
      return (
     <returnDoc>
      <invoice_id>{$return/invoice__id/text()}</invoice_id>
      <return_date>{$return/date/text()}</return_date>
      <days_to_Return>{$return/days__to__return/text()}</days_to_Return>
      <early_return>{$return/early__return/text()}</early_return>
    
    <products>
      {
        for $product in $return/productsReturned
        return
          <product>
            <product_code>{$product//id/text()}</product_code>
            <list_price>{$product//list__price/text()}</list_price>
            <brand>{$product//brand/text()}</brand>
            <model>{$product//model/text()}</model>
          </product>
      }
    </products>
    
    <salesAssociated>
      {
        for $sale in $return/sale__data
        return
          <sale_info>
            <invoice_id>{$sale//invoice__id/text()}</invoice_id>
            <date>{$sale//date/text()}</date>
            <customer_id>{$sale//date/text()}</customer_id>
          </sale_info>
      }
    </salesAssociated>
  </returnDoc>
      )  
    }
   </returns>
   </ReturnsReport>
};