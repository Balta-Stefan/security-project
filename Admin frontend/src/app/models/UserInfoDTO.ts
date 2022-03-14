import { Role } from "./Role";

export interface UserInfoDTO{
    id: number;
    role: Role;
    active: boolean;
    canCreate: boolean;
    canRead: boolean;
    canUpdate: boolean;
    canDelete: boolean;
    accessFromIp: string;
    accessFromDomain: string;
    username: string;
    rootDirID: number;
}