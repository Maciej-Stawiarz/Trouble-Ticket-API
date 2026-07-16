# How to start working with the application


## 1. Setting up variables in .env file
In the root layer of files, you can find a file named _.env.example_. This file serves as a template for the much needed _**.env**_ file, that
you need to create in order for the docker-compose.yml file to work. It contains properties, which go to the docker-compose.yml file, which cannot or rather, 
should not normally be commited to the repository (such as usernames and passwords).

To be able to start the database service, you need to create _**.env**_ file in the same folder as _docker-compose.yml_ file and
make sure all properties used in the services, that are marked with _**${}**_ are present in the .env file with
correct values.

Take note, as any value that you will input into those properties will become the name of the database, username and password of the user.

## 2. Starting database service
To start the database service, what you need to do is to finish all preparations mentioned in topic nr _**1**_. After you've done it,
in your console (cmd, local intellij IDEA console, etc.), navigate to the location in which docker-compose.yml file is located
and use _**docker compose up -d database**_ to create a container with the PostgreSQL database on it.

After it's done, you can use command: _**docker ps**_ from any location in the console to check, if the container is up.

_**ALTERNATIVELY**_

If you are using any IDE that supports docker-compose.yml files, like Intellij IDEA does, you can navigate to the docker-compose.yml file
in the IDEA and click **green arrow** next to the service to start it.

## 3. Setting up environmental variables for application.yml file (Only when running through IDE)
To set up variables in the _application.yml_ file, which will allow you to start the application, you need to either:

**OPTION 1**

Replace all the placeholders with actual values, that is:
- _${POSTGRES_USERNAME}_ with actual username, which you can get from the topic nr _**1**_
- _${POSTGRES_PASSWORD}_ with actual password, which you can get from the topic nr _**1**_
- _${JWT_SECRET}_ with jwt secret key, which you can generate locally in the console or online

**OPTION 2**

You will still have to replace those values, but instead of removing the placeholders, you can directly place
those values in the environmental variables. Intellij IDEA allows you to do that in the configuration of your start-up,
in the option _environmental variables_.


## 4. Starting application service
To start the application service, in your command line, go to the location of the _**Dockerfile**_ file. It should be located in the root level.
Once you are there, perform command: <br>

**_docker build -t ms/trouble_ticket_api ._**

to create an image of the application with the name _**ms/trouble_ticket_api**_.

After you have done it, all you have to do is to run command _**docker compose up -d application**_. 

## 5. Generating JWT token for requests
Since all requests need to be validated against JWT Bearer token and all other requests are to be invalidated,
we need a way to generate that token to help our request pass through. To achieve that, we need to do a couple of things:

1. First, we have to have defined _JWT_SECRET_ property in the environmental variables. This topic is covered in the major topic nr _**3**_.
2. Now, we need to go to the website _jwt.io_ and manually create the token.
3. We select the option to encode a token
4. We give it in the header:
   1. _"alg": "HS256"_
   2. _"typ": "JWT"_
5. In the payload:
   1. _"sub": "your-dummy-value-for-sub"_ (this bit doesn't really matter much)
   2. _"tenantId": "your-dummy-value-for-tenant-id"_ (this will be used across the application for validation)
   3. _"iat": "your-token-issued-time"_
   4. _"exp": "your-token-expiration-date"_

Finished token should look similar to this:

**Header:**
_{
"alg": "HS256",
"typ": "JWT"
}_

**Paload:**
_{
"sub": "test-user",
"tenantId": "7da745e4-48d2-4013-9015-5c8ba8452a0d",
"iat": 1784158085,
"exp": 1815694085
}_

**Secret:**
_Ppv2Gc10kAZrO80MXnP24EvRHhMpQgGGOZXfgoqW9GW_

**Final Token** (This one is valid for a year, so it can also just be used):
_eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJ0ZW5hbnRJZCI6IjdkYTc0NWU0LTQ4ZDItNDAxMy05MDE1LTVjOGJhODQ1MmEwZCIsImlhdCI6MTc4NDE1ODA4NSwiZXhwIjoxODE1Njk0MDg1fQ.P14960NX5KId0AzAuDgRrcBPtlNAW8mbgNCmbu9xmvk_