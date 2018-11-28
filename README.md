# Retrofit File Upload

## An example app of using retrofit for SINGLE/MULTIPLE file upload to server.

### Any type and any number of file(s) can be uploaded to server by following this app.

For server side code(php) use this: [File Upload Api](https://github.com/arhanashik/FileUploadApi)

## For testing how it works
- Clone this repo and open in android studio
- Clone the server side api and put that on xamp or any other server
- Follow the instruction for setup api given that api repo
- Change the server base url in android project(in RemoteUtil class - BASE_URL)
- Run the project and select any file using the fab button
- Select again if you wish to upload multiple file
- Click on the upload button on app bar. 
- You will see the uploaded file in a awesome auto-fit recycler view! 