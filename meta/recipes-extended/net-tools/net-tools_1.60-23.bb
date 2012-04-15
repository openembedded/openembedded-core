SUMMARY="Basic networking tools"
DESCRIPTION="A collection of programs that form the base set of the NET-3 networking distribution for the Linux operating system"
HOMEPAGE = "http://net-tools.berlios.de/"
BUGTRACKER = "http://bugs.debian.org/net-tools"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b \
                    file://ifconfig.c;startline=11;endline=15;md5=da4c7bb79a5d0798faa99ef869721f4a"
PR = "r0"

SRC_URI = "${DEBIAN_MIRROR}/main/n/net-tools/net-tools_1.60.orig.tar.gz;name=tarball \
           ${DEBIAN_MIRROR}/main/n/net-tools/${BPN}_${PV}.diff.gz;apply=no;name=patch \
           file://net-tools-config.h \
           file://net-tools-config.make" 

S = "${WORKDIR}/net-tools-1.60"

SRC_URI[tarball.md5sum] = "ecaf37acb5b5daff4bdda77785fd916d"
SRC_URI[tarball.sha256sum] = "ec67967cf7b1a3a3828a84762fbc013ac50ee5dc9aa3095d5c591f302c2de0f5"

SRC_URI[patch.md5sum] = "2412e55c20308d5fbd28bfadd18c075f"
SRC_URI[patch.sha256sum] = "d678b3ea97d6c7ca548918994642bfc6b5511ab02f3a5881dfcca00c88bfd73b"

inherit gettext

# The Makefile is lame, no parallel build
PARALLEL_MAKE = ""

# Unlike other Debian packages, net-tools *.diff.gz contains another series of
# patches maintained by quilt. So manually apply them before applying other local
# patches. Also remove all temp files before leaving, because do_patch() will pop 
# up all previously applied patches in the start
nettools_do_patch() {
	cd ${S}
	quilt pop -a || true
	if [ -d ${S}/.pc-nettools ]; then
		mv ${S}/.pc-nettools ${S}/.pc
		QUILT_PATCHES=${S}/debian/patches quilt pop -a
		rm -rf ${S}/.pc ${S}/debian
	fi
	patch -p1 < ${WORKDIR}/${BPN}_${PV}.diff	
	QUILT_PATCHES=${S}/debian/patches quilt push -a
	mv ${S}/.pc ${S}/.pc-nettools
}

do_unpack[cleandirs] += "${S}"

# We invoke base do_patch at end, to incorporate any local patch
python do_patch() {
	bb.build.exec_func('nettools_do_patch', d)
	bb.build.exec_func('patch_do_patch', d)
}

do_configure() {
	# net-tools has its own config mechanism requiring "make config"
	# we pre-generate desired options and copy to source directory instead
	cp ${WORKDIR}/net-tools-config.h    ${S}/config.h
	cp ${WORKDIR}/net-tools-config.make ${S}/config.make
}

do_compile() {
	# net-tools use COPTS/LOPTS to allow adding custom options
	export COPTS="$CFLAGS"
	export LOPTS="$LDFLAGS"
	unset CFLAGS
	unset LDFLAGS

	oe_runmake
}

do_install() {
	oe_runmake 'BASEDIR=${D}' install

	for app in ${D}/${base_sbindir}/* ${D}/${base_bindir}/*; do
		mv $app $app.${PN}
	done
}

pkg_postinst_${PN} () {
	for app in arp ifconfig ipmaddr iptunnel mii-tool nameif plipconfig rarp route slattach ; do
		update-alternatives --install ${base_sbindir}/$app $app $app.${PN} 100
	done

	for app in dnsdomainname domainname hostname netstat nisdomainname ypdomainname ; do
		update-alternatives --install ${base_bindir}/$app $app $app.${PN} 100
	done
}

pkg_prerm_${PN} () {
	for app in arp ifconfig ipmaddr iptunnel mii-tool nameif plipconfig rarp route slattach dnsdomainname domainname hostname netstat nisdomainname ypdomainname ; do
		update-alternatives --remove $app $app.${PN}
	done
}
