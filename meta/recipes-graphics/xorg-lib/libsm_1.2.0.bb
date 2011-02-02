SUMMARY = "SM: Session Management library"

DESCRIPTION = "The Session Management Library (SMlib) is a low-level \"C\" \
language interface to XSMP.  The purpose of the X Session Management \
Protocol (XSMP) is to provide a uniform mechanism for users to save and \
restore their sessions.  A session is a group of clients, each of which \
has a particular state."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=d98446615b962372d6a46057170043fa"

DEPENDS += "libice xproto xtrans e2fsprogs"

PR = "r1"
PE = "1"

XORG_PN = "libSM"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "e78c447bf1790552b644eca81b542742"
SRC_URI[sha256sum] = "0cd8df1b7067bfda10b05d38279777770677c6fecb5a14e804a28597da7a57cb"
