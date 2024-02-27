[33mcommit 4ac86d3e4e12abd337c019d439fcadb613beda64[m[33m ([m[1;36mHEAD -> [m[1;32mgroup_feature[m[33m, [m[1;31morigin/group_feature[m[33m)[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Tue Feb 27 11:02:52 2024 +0200

    delete user role form register request

[33mcommit b7b6db983fb3d5c5d36d357dafe7c40f17e2c190[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Tue Feb 27 10:54:38 2024 +0200

    use user service layer instead of repository layer directly

[33mcommit b3e5fa70a668dd683f352ca6a487811b64b1d78d[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Tue Feb 27 10:42:18 2024 +0200

    updaet application.properities

[33mcommit 7e0cbf55a091a6a82cd2e809bdce469339e677a8[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Tue Feb 27 10:40:36 2024 +0200

    handle exception handler

[33mcommit 7e846140fc9352c60f4897c7c93a40e3cf304dcb[m[33m ([m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Merge: 34b7b62 a631282
Author: zeyad2003 <zeyadnasef85@gmail.com>
Date:   Tue Feb 27 00:04:14 2024 +0200

    Merge branch 'Contest-Feature'
    
    This merge introduces the following changes:
    
    - Modify the problem schema to handle different online judges data.
    - Save the submission data into database and add the required DTOs for handle the information.
    - Add the implementation of the Contest feature with all logic & validation.
    .
    .
    .
    .
    - 🙃 وترابيزات بلياردو وبنج

[33mcommit 34b7b6233dea11d4951c223a3b9e238e3659ef83[m
Merge: cb206a3 ea56ce5
Author: Zeyad Nasef <87117386+Zeyad2003@users.noreply.github.com>
Date:   Mon Feb 26 22:27:39 2024 +0200

    Merge pull request #26 from yousofkortam/main
    
    Group feature - security

[33mcommit a631282940bfdca2b31fde197d9af39e8309b8d1[m
Author: abdo <magdyabdo@gmail.com>
Date:   Mon Feb 26 05:59:48 2024 +0200

    update submitInContest() validation -check if problem exist in contest or not-

[33mcommit 0211bdeeb7acbe95b46569c3b36b1b2add0f6c33[m
Author: abdo <magdyabdo@gmail.com>
Date:   Mon Feb 26 04:57:39 2024 +0200

    update handleContestProblemSetRelation() to validate duplication of problemHashtag to avoid deleting ContestProblem entity without updating

[33mcommit 53c11f4edbf9694904d11057ce50228f84ff49b3[m
Author: abdo <magdyabdo@gmail.com>
Date:   Mon Feb 26 04:32:09 2024 +0200

    update getContestProblem() to solve the problem of query does not return a unique data

[33mcommit 06e16c13afd6db98d0604972c134032fb2539c32[m
Author: abdo <magdyabdo@gmail.com>
Date:   Mon Feb 26 04:19:22 2024 +0200

    update getContestProblems() to get problem data directly to solve query duplication issue

[33mcommit f23a71ff7b8dcf6d715003aa6cfd805f7255fcc9[m
Author: zeyad2003 <zeyadnasef85@gmail.com>
Date:   Sun Feb 25 16:58:52 2024 +0200

    chore: make UniqueConstraint for ContestProblem table
    
    - 🫡 أحلي تحيه لبشمهندس عبدو صاحب النظره الثاقبه التي أتاحت رؤية هذه البجايه اللئيمه

[33mcommit b3711f51839934c42cfa779c46e362b10ddb638b[m
Author: zeyad2003 <zeyadnasef85@gmail.com>
Date:   Sun Feb 25 16:49:32 2024 +0200

    chore: Modify the `getContestProblem()` to make it reusable

[33mcommit f2bd84cee0aca872909db3dc64c842bf2631c72d[m
Author: zeyad2003 <zeyadnasef85@gmail.com>
Date:   Sun Feb 25 16:47:35 2024 +0200

    chore: add Browser.Version property to the application.properties

[33mcommit ea56ce5ad795879b9f8e442aea9bc0bcb82d7679[m
Merge: 13c2da4 e7e3766
Author: Yousof Kortam <115800563+yousofkortam@users.noreply.github.com>
Date:   Sun Feb 25 11:14:00 2024 +0200

    Merge pull request #10 from yousofkortam/group_feature
    
    handle token validation

[33mcommit e7e37661b65bbbdaadeadd4c9374a2c9aeb34c54[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Sun Feb 25 11:12:01 2024 +0200

    handle token validation

[33mcommit 80e67e03bb7c6d08613a9fb29d9f5ba8f24699e7[m
Author: abdo <magdyabdo@gmail.com>
Date:   Sun Feb 25 07:13:40 2024 +0200

    update updateContest() to solve the problem of saving contest modification before related entity and update handlers function

[33mcommit a18f4ca9070d72c7ed0a8692ed2ddc3ef8e972a7[m
Author: abdo <magdyabdo@gmail.com>
Date:   Sun Feb 25 06:10:02 2024 +0200

    upate creatContest() to solve the problem of saving contest before other related entity

[33mcommit 947891504a680fc51f9e4be6286fe517701c4360[m
Author: abdo <magdyabdo@gmail.com>
Date:   Sun Feb 25 06:03:43 2024 +0200

    Add Authentication object to get user by it and update model

[33mcommit 709e4af115e59728492196f8fa78cc23561bb71e[m
Author: abdo <magdyabdo@gmail.com>
Date:   Sun Feb 25 05:59:46 2024 +0200

    change problemHastag database constraint (Unique = false) as it make it unique in the table not contest

[33mcommit 13c2da4e3d2d65bb3dbef3d4b5afe1fecd0214a9[m
Merge: 5da32fe 8ee25fd
Author: Yousof Kortam <115800563+yousofkortam@users.noreply.github.com>
Date:   Sun Feb 25 02:38:41 2024 +0200

    Merge pull request #9 from yousofkortam/group_feature
    
    disable async execution globally

[33mcommit 8ee25fd1b202006d1cfcb036eb37f6659e4264b7[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Sun Feb 25 02:37:32 2024 +0200

    disable async execution globally

[33mcommit 5da32fe83d74b5e9460293920b2e184c04b69b48[m
Merge: 8b9aae7 90e0597
Author: Yousof Kortam <115800563+yousofkortam@users.noreply.github.com>
Date:   Sun Feb 25 02:25:02 2024 +0200

    Merge pull request #8 from yousofkortam/group_feature
    
    enable async execution globally

[33mcommit 90e059782acdf0e2858d685883dcabd900c0dfc1[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Sun Feb 25 02:23:29 2024 +0200

    enable async execution globally

[33mcommit 8b9aae70152aa35c2702d82ef7d24f223ece5f01[m
Merge: e99ca19 302a50e
Author: Yousof Kortam <115800563+yousofkortam@users.noreply.github.com>
Date:   Sun Feb 25 02:04:40 2024 +0200

    Merge pull request #7 from yousofkortam/group_feature
    
    add xjudge email

[33mcommit 302a50e2f3a5dbba84e7bd4c1d7bbfe107478de3[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Sun Feb 25 02:03:23 2024 +0200

    add xjudge email

[33mcommit e99ca1910c12b08d927cefb29b2fd410e645bce0[m
Merge: 3bcfdc6 abb6cda
Author: Yousof Kortam <115800563+yousofkortam@users.noreply.github.com>
Date:   Sun Feb 25 02:02:33 2024 +0200

    Merge pull request #6 from yousofkortam/group_feature
    
    increase thread pool task executor parameters to speed up email sending

[33mcommit abb6cda8ac61f3615df2bc82e9089b7985a7766f[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Sun Feb 25 02:01:46 2024 +0200

    increase thread pool task executor parameters to speed up email sending

[33mcommit 3bcfdc623ace13ce118ddd512666d196b761a97d[m
Merge: cb206a3 e1974e8
Author: Yousof Kortam <115800563+yousofkortam@users.noreply.github.com>
Date:   Sun Feb 25 00:32:00 2024 +0200

    Merge pull request #5 from yousofkortam/group_feature
    
    leave group security

[33mcommit e1974e8aa6421803f267315cb788631764b0a435[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Sun Feb 25 00:29:32 2024 +0200

    security handled in user group service layer

[33mcommit fd77522d084a34543b95b0a0b6109f3006892775[m
Author: yousofkortam <yousofkortam@outlook.com>
Date:   Sun Feb 25 00:19:59 2024 +0200

    add token service

[33mcommit c42a3efe7bc4513cdee970791cb56f09faa89adc[m
Author: zeyad2003 <zeyadnasef85@gmail.com>
Date:   Sat Feb 24 11:27:45 2024 +0200

    feat: Add get problem by hashtag (A, B, AA ..etc) end-point

[33mcommit cb206a30a20da4ad1799e6abdbedc37ca44f855a[m
Merge: ca63a8e 355b031
Author: Yousof Kortam <115800563+yousofkortam@users.noreply.github.com>
Date:   Fri Feb 23 22:59:37 2024 +0200

    Merge pull request #25 from yousofkortam/main
    
    Group Feature

[33mcommit 355b031a469c271725288377722f04990f46423b[m
Merge: b46c16e a1f6c7b
Author: Yousof Kortam <115800563+yousofkortam@users.noreply.github.com>
Date:   Fri Feb 23 22:57:18 2024 +0200

    Merge pull request #4 from yousofkortam/group_feature
    
    add authen