// import CryptoJS from 'crypto-js';

export class MyLocalStorage {
    //private static SECRET_KEY: string = import.meta.env.VITE_LOCAL_STORAGE_SECRET_KEY;


    // Encriptar e guardar item no localStorage

    /**
     * Stores an item in localStorage.
     * 
     * @param {string} key - The key under which the value will be stored.
     * @param {string} value - The value to store.
     * @returns {void}
     */
    static setItem(key: string, value: string): void {
        // localStorage.setItem(key, this.encrypt(value));
        localStorage.setItem(key, value)

    }

     /**
     * Retrieves an item from localStorage.
     * 
     * @param {string} key - The key of the value to retrieve.
     * @returns {string} - The value associated with the key, or an empty string if the key does not exist.
     */
    static getItem(key: string): string {
        const data = localStorage.getItem(key) || "";
        // return this.decrypt(data);
        return data
        return data;
    }

    // private static encrypt(txt: string): string {
    //     return CryptoJS.AES.encrypt(txt, this.SECRET_KEY).toString();
    // }
/*
    private static encrypt(txt: string): string {
        return CryptoJS.AES.encrypt(txt, this.SECRET_KEY).toString();
    }

    // private static decrypt(txtToDecrypt: string) {
    //     return CryptoJS.AES.decrypt(txtToDecrypt, this.SECRET_KEY).toString(CryptoJS.enc.Utf8);
    // }*/
}