<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->


GnuPG Signing.
==============

A digital signature certifies and timestamps a document. If the document is subsequently modified in any 
way, a verification of the signature will fail. A digital signature can serve the same purpose as a 
hand-written signature with the additional benefit of being tamper-resistant.

So we'll need to create a public/private keypair for every person who is responsible for generating a release
to maven central so that we can say "yes, this person released these artifacts." We'll go over below, how
to set up your public-private GPG keypair such that you can sign releases or anything for that matter.

### Installing GnuPG

By GPG, we refer to [Gnu Privacy Guard](https://www.gnupg.org/), which is an open implementation
to the OpenPGP standard as defined by [RFC4880](https://www.ietf.org/rfc/rfc4880.txt). For our purposes
we assume that the user is on a Mac and can run `brew install gpg`. But, if one needs to do something manual
one needs to install both __Pinentry__ as well as __GnuPG__ from  https://www.gnupg.org/download/index.html.

Install __pinentry mac__ via `brew install pinentry-mac`. 


#### Generating gpg keyfiles for signing.

We recommend that you follow the [github instructions](https://help.github.com/en/articles/generating-a-new-gpg-key)
for creating a new GPG key. Furthermore, we suggest that you use `RSA and RSA` with a keysize of `4096`, and
an expiration date `4y` in the future. Use your real name, your Capital One email address, and no comment.
You will be asked to use a *passphrase*. Clearly, we recommend a strong passphrase. GnuPG will then ask 
you to generate entropy.

#### Logging into your key.

We'll need to get our passphrase that we used above set up with pinentry, and thus we proceed with the following
commands:

```bash
$ echo "pinentry-program /usr/local/bin/pinentry-mac" >> ~/.gnupg/gpg-agent.conf
$ killall gpg-agent
```

which will set up your gpg-agent to rely upon pinentry for retrieving the password. Finally you'll run:

```bash
$ echo "test" | gpg --clearsign
```

which will cause pinentry to ask you for your passphrase. We suggest that you store your passphrase in your
keychain. Though note, doing this will no longer require that you enter your passphrase to sign things from
your command line which is a mild security risk. You can, on the other hand, enter your passphrase manually 
every time.

### Setting up github for gpg signing commits and maven builds.

You can list your gpg keys in the following way:

```bash
$gpg --list-secret-keys --keyid-format LONG
gpg: checking the trustdb
gpg: marginals needed: 3  completes needed: 1  trust model: pgp
gpg: depth: 0  valid:   1  signed:   0  trust: 0-, 0q, 0n, 0m, 0f, 1u
gpg: next trustdb check due at 2023-04-21
/Users/abcdefg/.gnupg/pubring.kbx
--------------------------------
sec   rsa4096/C512F178A71B4D90 2019-04-22 [SC] [expires: 2023-04-21]
      894ECF5B95DCE4636385EA20C512F178A71B4D90
uid                 [ultimate] My Name <my.name@somedomain.com>
ssb   rsa4096/9D4AEA4F32FFCF70 2019-04-22 [E] [expires: 2023-04-21]
```

which gives you the label of your private GPG signing key. To set git up to handle signing your commits
you'll have to set your gitconfig in the following way:

```bash
$ git config --global user.signingkey C512F178A71B4D90
```

From here you should be able to run:

```bash
git commit -S -a -m "My commit message"
```

for commits and:

```bash
git tag -s <my_tag_name> -m "My tag message"
```

for tags, each of which will add a signature to your commit or tag respectively. Note, you will not get credit in github until you put your gpg public
key into `Settings>SSH and GPG Keys>GPG Keys>New GPG Key.`

### Adding your public key to github.

Run the following to print your public key from the command line:

```bash
gpg --armor --export my.name@somedomain.com > ~/Desktop/mykey.asc && cat ~/Desktop/mykey.asc
```

This will print something that looks like:

```
-----BEGIN PGP PUBLIC KEY BLOCK-----
Version: GnuPG v1.2.1 (GNU/Linux)
Comment: For info see http://www.gnupg.org
	
mQGiBDkHP3URBACkWGsYh43pkXU9wj/X1G67K8/DSrl85r7dNtHNfLL/ewil10k2
q8saWJn26QZPsDVqdUJMOdHfJ6kQTAt9NzQbgcVrxLYNfgeBsvkHF/POtnYcZRgL
tZ6syBBWs8JB4xt5V09iJSGAMPUQE8Jpdn2aRXPApdoDw179LM8Rq6r+gwCg5ZZa
pGNlkgFu24WM5wC1zg4QTbMD/3MJCSxfL99Ek5HXcB3yhj+o0LmIrGAVBgoWdrRd
BIGjQQFhV1NSwC8YhN/4nGHWpaTxgEtnb4CI1wI/G3DK9olYMyRJinkGJ6XYfP3b
cCQmqATDF5ugIAmdditnw7deXqn/eavaMxRXJM/RQSgJJyVpbAO2OqKe6L6Inb5H
kjcZA/9obTm499dDMRQ/CNR92fA5pr0zriy/ziLUow+cqI59nt+bEb9nY1mfmUN6
SW0jCH+pIQH5lerV+EookyOyq3ocUdjeRYF/d2jl9xmeSyL2H3tDvnuE6vgqFU/N
sdvby4B2Iku7S/h06W6GPQAe+pzdyX9vS+Pnf8osu7W3j60WprQkUGF1bCBHYWxs
YWdoZXIgPHBhdWxnYWxsQHJlZGhhdC5jb20+iFYEExECABYFAjkHP3UECwoEAwMV
AwIDFgIBAheAAAoJEJECmvGCPSWpMjQAoNF2zvRgdR/8or9pBhu95zeSnkb7AKCm
/uXVS0a5KoN7J61/1vEwx11poLkBDQQ5Bz+MEAQA8ztcWRJjW8cHCgLaE402jyqQ
37gDT/n4VS66nU+YItzDFScVmgMuFRzhibLblfO9TpZzxEbSF3T6p9hLLnHCQ1bD
HRsKfh0eJYMMqB3+HyUpNeqCMEEd9AnWD9P4rQtO7Pes38sV0lX0OSvsTyMG9wEB
vSNZk+Rl+phA55r1s8cAAwUEAJjqazvk0bgFrw1OPG9m7fEeDlvPSV6HSA0fvz4w
c7ckfpuxg/URQNf3TJA00Acprk8Gg8J2CtebAyR/sP5IsrK5l1luGdk+l0M85FpT
/cen2OdJtToAF/6fGnIkeCeP1O5aWTbDgdAUHBRykpdWU3GJ7NS6923fVg5khQWg
uwrAiEYEGBECAAYFAjkHP4wACgkQkQKa8YI9JamliwCfXox/HjlorMKnQRJkeBcZ
iLyPH1QAoI33Ft/0HBqLtqdtP4vWYQRbibjW
=BMEc
-----END PGP PUBLIC KEY BLOCK-----
```

which is what you will put in github or a `KEYS` file associated with a project.