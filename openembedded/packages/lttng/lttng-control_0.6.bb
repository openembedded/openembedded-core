SECTION = "devel"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
LICENSE = "GPL"
MAINTAINER = "Richard Purdie <rpurdie@rpsys.net>"

SRC_URI = "http://ltt.polymtl.ca/lttng/ltt-control-${PV}-28042006.tar.gz"

S = "${WORKDIR}/ltt-control-${PV}-28042006"

inherit autotools
#inherit module

export KERNELDIR="${STAGING_KERNEL_DIR}"

#INHIBIT_PACKAGE_STRIP = "1"
#EXTRA_OECONF = "--without-rtai \
#		--without-gtk"

#do_configure () {
#	rm -f ${S}/acinclude.m4
#	autotools_do_configure
#}

#o_install() {   
#       install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/net/
#       install -m 0644 *${KERNEL_OBJECT_SUFFIX} ${D}${base_libdir}/modules/${KERNEL_VERSION}/net/
#


#ILES = "/lib/modules/${KERNEL_VERSION}/net/*_cs${KERNEL_OBJECT_SUFFIX}"        
FILES_${PN} += "${datadir}/ltt-control/facilities/*"	    