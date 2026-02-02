SUMMARY = "CVE List V5"
DESCRIPTION = "Official CVE List. It is a catalog of all CVE Records identified by, or reported to, the CVE Program. \
The cvelistV5 repository hosts downloadable files of CVE Records in the CVE Record Format."
HOMEPAGE = "https://github.com/CVEProject/cvelistV5"
LICENSE = "cve-tou"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/cve-tou;md5=4f7e96b3094e80e66b53359a8342c7f8"

inherit allarch native

SRC_URI = "git://github.com/CVEProject/cvelistV5.git;branch=main;protocol=https"

# SRCREV is pinned to a fixed commit to ensure reproducible builds
# To get the latest commit available and stay up-to-date, set AUTOREV as SRCREV with SRCREV:pn-cvelistv5-native = "${AUTOREV}"
SRCREV ?= "644ce1758db1773336ebebb6a0da90e132da0eb7"

do_install(){
	install -d ${D}${datadir}/cvelistv5-native
	cp -r ${UNPACKDIR}/cvelistv5-git/* ${D}${datadir}/cvelistv5-native/
}
