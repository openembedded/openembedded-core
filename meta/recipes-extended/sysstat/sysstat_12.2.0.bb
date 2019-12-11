require sysstat.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a23a74b3f4caf9616230789d94217acb"

SRC_URI += "file://0001-Include-needed-headers-explicitly.patch \
            file://0001-configure.in-remove-check-for-chkconfig.patch \
            file://CVE-2019-19725.patch \
"

SRC_URI[md5sum] = "7deffb18e7f32a0b74ab81f1f75de9ee"
SRC_URI[sha256sum] = "61aa98eb8b38542eb97defcc2472adee1a24df8252f41b96e20d64c6064a8375"
