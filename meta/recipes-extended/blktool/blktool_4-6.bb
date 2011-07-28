SUMMARY = "Tune low-level block device parameters"
DESCRIPTION = "blktool is used for querying and/or changing settings \
of a block device. It is like hdparm but a more general tool, as it \
works on SCSI, IDE and SATA devices."
HOMEPAGE = "http://packages.debian.org/unstable/admin/blktool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://blktool.c;beginline=7;endline=8;md5=a5e798ea98fd50972088968a15e5f373"

DEPENDS = "glib-2.0"
PR = "r0"

SRC_URI = "${DEBIAN_MIRROR}/main/b/blktool/blktool_4.orig.tar.gz;name=tarball \
           ${DEBIAN_MIRROR}/main/b/blktool/blktool_4-6.diff.gz;apply=yes;name=patch"

SRC_URI[tarball.md5sum] = "62edc09c9908107e69391c87f4f3fd40"
SRC_URI[tarball.sha256sum] = "b1e6d5912546d2a4b704ec65c2b9664aa3b4663e7d800e06803330335a2cb764"

SRC_URI[patch.md5sum] = "2d1bc2f2c38b65d47e27da7c7508d17f"
SRC_URI[patch.sha256sum] = "999f2062203e389327d997724621be37bea9c98fa226238f9f4eb4a6ea25bd4b"

S = "${WORKDIR}/${BPN}-4.orig"

inherit autotools
