require bash.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=fd5d9bcabd8ed5a54a01ce8d183d592a"
PR = "r6"

SRC_URI = "${GNU_MIRROR}/bash/bash-${PV}.tar.gz \
           http://ftp.gnu.org/gnu/bash/bash-3.2-patches/bash32-049;apply=yes;striplevel=0 \
           http://ftp.gnu.org/gnu/bash/bash-3.2-patches/bash32-050;apply=yes;striplevel=0 \
           http://ftp.gnu.org/gnu/bash/bash-3.2-patches/bash32-051;apply=yes;striplevel=0 \
           "
