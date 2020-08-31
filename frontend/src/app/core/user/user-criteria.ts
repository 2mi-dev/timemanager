export interface UserCriteria {
  page: number;
  size: number;
  sort?: string;
  filter?: string;
  username?: string;
  nickname?: string;
  name?: string;
  email?: string;
  enabled?: boolean;
  activated?: boolean;
  authority?: string;
}
