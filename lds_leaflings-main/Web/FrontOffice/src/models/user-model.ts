export interface User {
    id: number,
    username: string,
    email: string,
    contact: string,
    rolePaid: boolean,
    location: string,
    careExperience: "Beginner" | "Intermediate" | "Expert",
    waterAvailability: "Low" | "Medium" | "High",
    luminosityAvailability: "Low" | "Medium" | "High",
    userAvatar: string

}

export interface UserPreferences {
    waterAvailability: "Low" | "Medium" | "High",
    luminosityAvailability: "Low" | "Medium" | "High",
    careExperience: "Beginner" | "Intermediate" | "Expert",
}