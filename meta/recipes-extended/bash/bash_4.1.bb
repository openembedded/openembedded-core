require bash.inc

PR = "r0"

SRC_URI = "${GNU_MIRROR}/bash/${BPN}-${PV}.tar.gz \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-001;apply=yes;striplevel=0 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-002;apply=yes;striplevel=0 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-003;apply=yes;striplevel=0 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-004;apply=yes;striplevel=0 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-005;apply=yes;striplevel=0 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-006;apply=yes;striplevel=0 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-007;apply=yes;striplevel=0 \
           "
