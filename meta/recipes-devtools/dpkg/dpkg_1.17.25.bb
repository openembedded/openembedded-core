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
            file://add_armeb_triplet_entry.patch \
           "

SRC_URI[md5sum] = "e48fcfdb2162e77d72c2a83432d537ca"
SRC_URI[sha256sum] = "07019d38ae98fb107c79dbb3690cfadff877f153b8c4970e3a30d2e59aa66baa"

