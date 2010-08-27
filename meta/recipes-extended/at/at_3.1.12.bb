require at.inc

LICENSE="GPLv2+ & GPLv3+"
LIC_FILES_CHKSUM = "file://Copyright;md5=dffa11c636884752fbf0b2a159b2883a"

PR = "r0"

SRC_URI = "${DEBIAN_MIRROR}/main/a/at/at_${PV}.orig.tar.gz \
    file://configure.patch \
    file://nonrootinstall.patch \
    file://use-ldflags.patch"

SRC_URI[md5sum] = "1e67991776148fb319fd77a2e599a765"
SRC_URI[sha256sum] = "7c55c6ab4fbe8add9e68f31b2b0ebf3fe805c9a4e7cfb2623a3d8a4789cc18f3"

EXTRA_OECONF += "--with-jobdir=/var/spool/cron/atjobs \
                 --with-atspool=/var/spool/cron/atspool \
                 --with-daemon_username=root \
                 --with-daemon_groupname=root \
                "
