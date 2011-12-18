SUMMARY = "NFS id mapping library"
DESCRIPTION = "NFS id mapping library"
HOMEPAGE = "http://www.citi.umich.edu/projects/nfsv4/linux/"
SECTION = "libs"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=d9c6a2a0ca6017fda7cd905ed2739b37"
PR = "r0"

SRC_URI = "http://www.citi.umich.edu/projects/nfsv4/linux/libnfsidmap/${BPN}-${PV}.tar.gz \
           file://fix-ac-prereq.patch \
          "

SRC_URI[md5sum] = "d71a1ee9881d5b5814ff3ec41256937d"
SRC_URI[sha256sum] = "59501432e683336d7a290da13767e92afb5b86f42ea4254041225fe218e8dd47"

inherit autotools

EXTRA_OECONF = "--disable-ldap"
