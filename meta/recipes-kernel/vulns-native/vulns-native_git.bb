SUMMARY = "Linux Security Vulns Repo"
DESCRIPTION = "Repo for tracking and maintaining the CVE identifiers reserved and assigned to \
the Linux kernel project."
HOMEPAGE = "https://git.kernel.org/pub/scm/linux/security/vulns.git/"
LICENSE = "cve-tou"
LIC_FILES_CHKSUM = "file://LICENSES/cve-tou.txt;md5=0d1f8ff7666c210e0b0404fd9d7e6703"

inherit allarch native

SRC_URI = "git://git.kernel.org/pub/scm/linux/security/vulns.git;branch=master;protocol=https"

# SRCREV is pinned to a fixed commit to ensure reproducible builds
# To get the latest commit available and stay up-to-date, set AUTOREV as SRCREV with SRCREV:pn-vulns-native = "${AUTOREV}"
SRCREV ?= "2c9b20d7a0699222b58c4824560b716b6096637b"

do_install(){
	install -d ${D}${datadir}/vulns-native
	cp -r ${UNPACKDIR}/vulns-git/* ${D}${datadir}/vulns-native/
}
