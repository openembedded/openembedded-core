require xorg-proto-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=b9f79c119df756aeffcb89ec96716a9e \
                    file://XI2proto.h;endline=48;md5=1ac1581e61188da2885cc14ff49b20be"

PR = "r2"
PE = "1"

inherit gettext

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "0f7acbc14a082f9ae03744396527d23d"
SRC_URI[sha256sum] = "472f57f7928ab20a1303a25982c4091db9674c2729bbd692c9a7204e23ea1af4"
