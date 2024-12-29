import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { api } from "@/contexts/AuthContext";
import { useState } from "react";

const UploadFile = () => {
    const [file, setFile] = useState<File | null>(null);

    const getFileImagePreview = () => URL.createObjectURL(file as Blob) ?? "";

    const handleInputFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {
            target: { files },
        } = e;
        console.log(files);
        if (files?.length) {
            setFile(files[0]);
        }
    };

    const handleFileUpload = () => {
        if (file) {
            const formData = new FormData();
            formData.append("file", file);
            api.post("/upload", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
                onUploadProgress: (progressEvent) => {
                    progressEvent.total &&
                        console.log(Math.round((progressEvent.loaded * 100) / progressEvent.total));
                },
            })
                .then((res) => {
                    console.log(res.data);
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    };

    return (
        <div className="container max-w-md mx-auto py-6 space-y-5">
            <div className="mb-4">
                <h1 className="font-bold text-2xl">Upload a file</h1>
                <p>Upload a new file</p>
            </div>
            <label htmlFor="file-input" className="cursor-pointer">
                <Input id="file-input" type="file" onChange={handleInputFileChange} multiple />
                {file && <img src={getFileImagePreview()} />}
            </label>
            <Button onClick={handleFileUpload}>Carica file</Button>
        </div>
    );
};

export default UploadFile;
