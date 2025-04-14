export interface Invitation {
    id: number;
    code: string;
    admin: boolean;
    createdAt: string;
    invokedAt?: string;
    acceptedAt?: string;
}

export interface Version {
    id: number;
    version: string;
    downloadUrl: string;
    code: string;
    description: string;
    timestamp: string;
}
