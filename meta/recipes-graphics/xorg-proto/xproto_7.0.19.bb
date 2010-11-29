require xorg-proto-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=70c104816b85de375ba1fc09199d0e69"

PR = "r1"
PE = "1"

EXTRA_OECONF_append = "--enable-specs=no"
BBCLASSEXTEND = "native nativesdk"
