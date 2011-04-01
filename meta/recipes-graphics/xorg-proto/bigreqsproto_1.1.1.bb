require xorg-proto-common.inc

PR = "r0"
PE = "1"

SUMMARY = "BigReqs: X Big Requests extension headers"

DESCRIPTION = "This package provides the wire protocol for the \
BIG-REQUESTS extension, used to send larger requests that usual in order \
to avoid fragmentation."

BBCLASSEXTEND = "native nativesdk"

LIC_FILES_CHKSUM = "file://COPYING;md5=b12715630da6f268d0d3712ee1a504f4"

SRC_URI[md5sum] = "6f6c24436c2b3ab235eb14a85b9aaacf"
SRC_URI[sha256sum] = "1c3b85872b58d215e8fbfdc7c36bb397d10053324a5df8722227d35254fff09a"
