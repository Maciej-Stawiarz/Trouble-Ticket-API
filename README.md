# How to start working with the application


## 1. Setting up variables in .env file
## 2. Starting database service
## 3. Setting up environmental variables for application.yml file
## 4. Starting application service
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