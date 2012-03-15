SUMMARY = "SM: Session Management library"

DESCRIPTION = "The Session Management Library (SMlib) is a low-level \"C\" \
language interface to XSMP.  The purpose of the X Session Management \
Protocol (XSMP) is to provide a uniform mechanism for users to save and \
restore their sessions.  A session is a group of clients, each of which \
has a particular state."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=c0fb37f44e02bdbde80546024400728d"

DEPENDS += "libice xproto xtrans e2fsprogs"

PR = "r0"
PE = "1"

XORG_PN = "libSM"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "766de9d1e1ecf8bf74cebe2111d8e2bd"
SRC_URI[sha256sum] = "93c11d569c64f40723b93b44af1efb474a0cfe92573b0c8c330343cabb897f1d"
