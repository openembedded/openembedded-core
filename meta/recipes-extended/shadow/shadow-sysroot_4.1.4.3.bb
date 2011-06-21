SUMMARY = "Shadow utils requirements for useradd.bbclass"
DESCRIPTION = "Shadow utils requirements for useradd.bbclass"
HOMEPAGE = "http://pkg-shadow.alioth.debian.org"
BUGTRACKER = "https://alioth.debian.org/tracker/?group_id=30580"
SECTION = "base utils"
PRIORITY = "optional"
LICENSE = "BSD | Artistic"
LIC_FILES_CHKSUM = "file://login.defs_shadow-sysroot;md5=25e2f2de4dfc8f966ac5cdfce45cd7d5"

DEPENDS = "base-passwd"

PR = "r0"

# The sole purpose of this recipe is to provide the /etc/login.defs
# file for the target sysroot - needed so the shadow-native utilities
# can add custom users/groups for recipes that use inherit useradd.
SRC_URI = "file://login.defs_shadow-sysroot"

SRC_URI[md5sum] = "b8608d8294ac88974f27b20f991c0e79"
SRC_URI[sha256sum] = "633f5bb4ea0c88c55f3642c97f9d25cbef74f82e0b4cf8d54e7ad6f9f9caa778"

S = "${WORKDIR}"
SSTATEPOSTINSTFUNCS += "shadow_sysroot_sstate_postinst"

do_install() {
	install -d ${D}${sysconfdir}
	install -p -m 755 ${S}/login.defs_shadow-sysroot ${D}${sysconfdir}/login.defs
}

shadow_sysroot_sstate_postinst() {
	if [ "${BB_CURRENTTASK}" = "populate_sysroot" -o "${BB_CURRENTTASK}" = "populate_sysroot_setscene" ]
	then
		# Staging does not copy ${sysconfdir} files into the
		# target sysroot, so we need to do so manually. We
		# put this file in the target sysroot so it can be
		# used by recipes which use custom user/group
		# permissions.
		install -d -m 755 ${STAGING_DIR_TARGET}${sysconfdir}
		install -p -m 644 ${D}${sysconfdir}/login.defs ${STAGING_DIR_TARGET}/${sysconfdir}/login.defs
	fi
}
