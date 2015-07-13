require dpkg.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI += "file://noman.patch \
            file://check_snprintf.patch \
            file://check_version.patch \
            file://preinst.patch \
            file://fix-timestamps.patch \
            file://remove-tar-no-timestamp.patch \
            file://fix-abs-redefine.patch \
            file://arch_pm.patch \
            file://dpkg-configure.service \
            file://glibc2.5-sync_file_range.patch \
            file://no-vla-warning.patch \
            file://dpkg-1.17.4-CVE-2014-0471.patch \
            file://dpkg-1.17.4-CVE-2014-0471-CVE-2014-3127.patch \
            file://tarfix.patch \
           "

SRC_URI[md5sum] = "cc25086e1e3bd9512a95f14cfe9002e1"
SRC_URI[sha256sum] = "01cdc81c33e77c3d7c40df17e19171794542be7cf12e411381ffcaa8f87b1854"

