import {User, UserRequest} from "@/utils/types";
import {updateFail, updateSuccess} from "@/apis/auth/responseConstants";
import {handleGetUser, handleUpdateUser} from "./userAPI";

const getCurrentUser = () => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");
    return handleGetUser(username!, password!)
        .then((response) => {
            return response.data;
        }) as Promise<User>;
};

const updateCurrentUser = (user: UserRequest) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");
    return handleUpdateUser(user, username!, password!)
        .then((response) => {
            return {status: response.status, user: response.data, message: updateSuccess};
        })
        .catch((err) => {
            return {user: null, status: err.status, message: updateFail};
        });
};

export const UserService = {
    getCurrentUser,
    updateCurrentUser,
};