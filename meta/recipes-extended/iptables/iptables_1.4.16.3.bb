SUMMARY = "Tools for managing kernel packet filtering capabilities"
DESCRIPTION = "iptables is the userspace command line program used to configure and control network packet \
filtering code in Linux."
HOMEPAGE = "http://www.netfilter.org/"
BUGTRACKER = "http://bugzilla.netfilter.org/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263\
                    file://iptables/iptables.c;firstline=13;endline=25;md5=f1028f2401da1c120df27594b2be50ef"

PR = "r0"

RRECOMMENDS_${PN} = "kernel-module-x-tables \
                     kernel-module-ip-tables \
                     kernel-module-iptable-filter \
                     kernel-module-iptable-nat \
                     kernel-module-nf-defrag-ipv4 \
                     kernel-module-nf-conntrack \
                     kernel-module-nf-conntrack-ipv4 \
                     kernel-module-nf-nat \
                     kernel-module-ipt-masquerade"
FILES_${PN} =+ "${libdir}/xtables/ ${datadir}/xtables"
FILES_${PN}-dbg =+ "${libdir}/xtables/.debug"

SRC_URI = "http://netfilter.org/projects/iptables/files/iptables-${PV}.tar.bz2 \
           file://types.h-add-defines-that-are-required-for-if_packet.patch"

SRC_URI[md5sum] = "6dd40c12fb8f4c4312a0f8434369fbcd"
SRC_URI[sha256sum] = "643ccf34099d53d5b839e1d889c05627745a51ec122648e76a9fcec3a8a9ec79"

inherit autotools

EXTRA_OECONF = "--with-kernel=${STAGING_INCDIR} \
                ${@base_contains('DISTRO_FEATURES', 'ipv6', '', '--disable-ipv6', d)}"

do_configure_prepend() {
	# Remove some libtool m4 files
	# Keep ax_check_linker_flags.m4 which belongs to autoconf-archive.
	rm -f libtool.m4 lt~obsolete.m4 ltoptions.m4 ltsugar.m4 ltversion.m4
}
