export interface User {
  id?: any;
  username?: string;
  nickname?: string;
  name?: string;
  email?: string;
  phone?: string;
  avatar?: string;
  activated?: boolean;
  enabled?: boolean;
  langKey?: string;
  authorities?: string[];
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  password?: string;

}
