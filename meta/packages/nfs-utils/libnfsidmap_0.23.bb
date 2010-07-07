DESCRIPTION = "nfs idmapping library"
HOMEPAGE = "http://www.citi.umich.edu/projects/nfsv4/linux/"
SECTION = "libs"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=d9c6a2a0ca6017fda7cd905ed2739b37"
PR = "r0"

SRC_URI = "http://www.citi.umich.edu/projects/nfsv4/linux/libnfsidmap/${BPN}-${PV}.tar.gz"

inherit autotools

EXTRA_OECONF = "--disable-ldap"
