import { MyLocalStorage } from "../services/myLocalStorage"

export const useAuth = function (): boolean {
    const authToken = MyLocalStorage.getItem('authToken')

    if(authToken) {
        return true
    }

    return false
}