export type User = {
  username: string;
};

export type Scene = {
  id: number;
  dialog: string[];
  img: string;
  choices: Choice[];
};

export type Choice = {
    id: number
    name: string
    destination_id: number    
};

export interface IProps {
    testMethod?: Function
};

export interface IState {
    currentScene: number
};