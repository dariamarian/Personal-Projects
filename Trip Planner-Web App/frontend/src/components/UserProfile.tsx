import {Button} from "@/components/ui/button";
import React, {useEffect, useState} from "react";
import {UserService} from "@/apis/profile/UserService";
import {Gender} from "@/utils/types";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {toast} from "@/components/ui/use-toast";

const UserProfile: React.FC = () => {

    const [updateScreen, setUpdateScreen] = useState(false);

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [gender, setGender] = useState<Gender>(Gender.MALE);
    const [description, setDescription] = useState("");

    const [updateName, setUpdateName] = useState("");
    const [updateEmail, setUpdateEmail] = useState("");
    const [updateGender, setUpdateGender] = useState<Gender>(Gender.MALE);
    const [updateDescription, setUpdateDescription] = useState("");

    useEffect(() => {
        loadUserInfo();
    }, []);

    const loadUserInfo = async () => {
        // get user info from srv
        await UserService.getCurrentUser()
            .then((user) => {
                setName(user.name);
                setEmail(user.email);
                setGender(user.gender);
                setDescription(user.description);
            });
    };

    const switchToUpdateScreenHandler = () => {
        setUpdateName(name);
        setUpdateEmail(email);
        setUpdateGender(gender);
        setUpdateDescription(description);
        setUpdateScreen(true);
    }

    const switchToProfileScreenHandler = () => {
        setUpdateScreen(false);
    }

    function validateInput() {
        let err = "";
        let c = 0;
        if (updateName.length < 3) {
            err = err + "Name, ";
            c++;
        }
        if (updateEmail.length < 3) {
            err = err + "Email, ";
            c++;
        }
        if (updateDescription.length < 3) {
            err = err + "Description, ";
            c++;
        }
        if (c > 0) {
            err = err.substring(0, err.length - 2);
            c == 1 ? showErrorToast(err + " is too short!") : showErrorToast(err + " are too short!");
        }
        return c == 0;
    }

    const showErrorToast = (error: string) => {
        toast({
            title: "Error",
            variant: "destructive",
            description: error,
        })
    }

    const saveChanges = async () => {
        // user validation
        if (!validateInput()) {
            return;
        }
        // send to srv
        const {user, status, message} = await UserService.updateCurrentUser({
            name: updateName, email: updateEmail,
            gender: updateGender, description: updateDescription
        });

        if (status == 200) {
            setName(user.name);
            setEmail(user.email);
            setGender(user.gender);
            setDescription(user.description);
        }

        toast({
            title: status === 200 ? "Success" : "Error",
            variant: status === 200 ? "default" : "destructive",
            description: message,
        })
        // go back to update screen
        setUpdateScreen(false);
    }

    return (
        <div>
            <div>
                <ul>
                    <li className="mt-3">
                        <label className="text-sm">
                            Name:
                        </label>
                    </li>
                    <li>
                        <input
                            type="text"
                            className="font-nunito text-sm border-[#3CACAE] rounded-xl border-[1px] w-full p-1"
                            disabled={!updateScreen}
                            value={!updateScreen ? name : updateName}
                            onChange={(e) => setUpdateName(e.target.value)}
                        />
                    </li>

                    <li className="mt-3">
                        <label className="text-sm">
                            Email:
                        </label>
                    </li>
                    <li>
                        <input
                            type="text"
                            className="font-nunito text-sm border-[#3CACAE] rounded-xl border-[1px] w-full p-1"
                            disabled={!updateScreen}
                            value={!updateScreen ? email : updateEmail}
                            onChange={(e) => setUpdateEmail(e.target.value)}
                        />
                    </li>

                    <li className="mt-1">
                        <label className="text-sm">
                            Gender:
                        </label>
                    </li>
                    <li>
                        <Select
                            onValueChange={(e: Gender) => setUpdateGender(e)}
                            value={!updateScreen ? gender : updateGender}
                            disabled={!updateScreen}
                        >
                            <SelectTrigger
                                className="font-nunito text-sm disabled:border-[1px]  disabled:border-red border-[#3CACAE] rounded-xl border-[1px]">
                                <SelectValue placeholder="Choose"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value={Gender.MALE}>Male</SelectItem>
                                <SelectItem value={Gender.FEMALE}>Female</SelectItem>
                                <SelectItem value={Gender.OTHER}>Other</SelectItem>
                            </SelectContent>
                        </Select>
                    </li>

                    <li className="mt-3">
                        <label className="text-sm">
                            Description:
                        </label>
                    </li>
                    <li>
              <textarea
                  className="font-nunito text-sm border-[#3CACAE] rounded-xl border-[1px] w-full resize-none p-1"
                  placeholder="Tell us about yourself"
                  rows={5}
                  disabled={!updateScreen}
                  value={!updateScreen ? description : updateDescription}
                  onChange={(e) => setUpdateDescription(e.target.value)}
              />
                    </li>

                    {
                        updateScreen ? (
                            <div className="w-full">
                                <Button className="bg-[#3CACAE] w-1/2 rounded-full hover:bg-[#C26DBC]"
                                        onClick={switchToProfileScreenHandler}>
                                    Cancel
                                </Button>
                                <Button className="bg-[#3CACAE] w-1/2 rounded-full hover:bg-[#C26DBC]"
                                        onClick={saveChanges}>
                                    Save
                                </Button>
                            </div>
                        ) : (<> </>)
                    }

                </ul>

                {
                    !updateScreen ? (
                        <Button className="bg-[#3CACAE] w-full rounded-full hover:bg-[#C26DBC]"
                                onClick={switchToUpdateScreenHandler}>
                            Update information
                        </Button>
                    ) : (<> </>)
                }

            </div>
        </div>
    );
};

export default UserProfile;
