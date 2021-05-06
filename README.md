Chia blockchain statistics collector to MongoDB.

Write for easy plots counting (chia GUI doesn't show harvester plots).

This is draft write for my internal task.

Java don't support PKCS1 keys you need to convert it from PKCS1 to PKCS8:

openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in private_harvester.key -out private_harvester.key.pkcs8

Or use Bouncy Castle for support it.

Java 11 required

XCH wallet xch14r5g3dd48lgl9kan4wzhmddu6649c5yzv746y06vl4sdtp6tttvqkhnvqq
