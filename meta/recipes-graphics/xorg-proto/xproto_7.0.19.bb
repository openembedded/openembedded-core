require xorg-proto-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=70c104816b85de375ba1fc09199d0e69"

PR = "r1"
PE = "1"

EXTRA_OECONF_append = "--enable-specs=no"
BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "700f8663c23713c2a81a6038a7d358f0"
SRC_URI[sha256sum] = "47b14f6da8c57a726ef1cfa5964a4a6cf9505bc6d78f69d3ae89f4b19956fc2a"
