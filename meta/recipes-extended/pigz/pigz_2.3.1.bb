require pigz.inc
LIC_FILES_CHKSUM = "file://pigz.c;beginline=7;endline=21;md5=a21d4075cb00ab4ca17fce5e7534ca95"

SRC_URI[md5sum] = "e803f8bc0770c7a5e96dccb1d2dd2aab"
SRC_URI[sha256sum] = "629b0ce5422a3978f31742bf8275d0be2f84987140d18f390f1e3b4e46e0af54"

NATIVE_PACKAGE_PATH_SUFFIX = "/${PN}"

BBCLASSEXTEND = "native nativesdk"

