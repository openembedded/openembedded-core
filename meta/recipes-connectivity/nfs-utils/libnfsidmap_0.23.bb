DESCRIPTION = "nfs idmapping library"
HOMEPAGE = "http://www.citi.umich.edu/projects/nfsv4/linux/"
SECTION = "libs"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=d9c6a2a0ca6017fda7cd905ed2739b37"
PR = "r0"

SRC_URI = "http://www.citi.umich.edu/projects/nfsv4/linux/libnfsidmap/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "28f3ece648c1dc5d25e8d623d55f8bd6"
SRC_URI[sha256sum] = "69d20cfc6be6bf7ede2a55da687dc6853f2db6c8ef826a0b5e3235e7d46f4051"

inherit autotools

EXTRA_OECONF = "--disable-ldap"
