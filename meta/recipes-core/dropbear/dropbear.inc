DESCRIPTION = "Dropbear is a lightweight SSH and SCP implementation"
HOMEPAGE = "http://matt.ucc.asn.au/dropbear/dropbear.html"
SECTION = "console/network"

# some files are from other projects and have others license terms:
#   public domain, OpenSSH 3.5p1, OpenSSH3.6.1p2, PuTTY
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3a5b0c2f0d0c49dfde9558ae2036683c"

DEPENDS = "zlib"
RPROVIDES = "ssh sshd"

SRC_URI = "http://matt.ucc.asn.au/dropbear/releases/dropbear-${PV}.tar.gz \
	         file://urandom-xauth-changes-to-options.h.patch \
	         file://configure.patch \
	         file://fix-2kb-keys.patch \
	         file://allow-nopw.patch;apply=no \
	         file://init"

inherit autotools update-rc.d

INITSCRIPT_NAME = "dropbear"
INITSCRIPT_PARAMS = "defaults 10"

CFLAGS_prepend = " -I. "
LD = "${CC}"

SBINCOMMANDS = "dropbear dropbearkey dropbearconvert"
BINCOMMANDS = "dbclient ssh scp"
EXTRA_OEMAKE = 'MULTI=1 SCPPROGRESS=1 PROGRAMS="${SBINCOMMANDS} ${BINCOMMANDS}"'

DISTRO_TYPE = "${@base_contains("IMAGE_FEATURES", "debug-tweaks", "debug", "",d)}"

do_debug_patch() {
	if [ "${DISTRO_TYPE}" = "debug" ]; then
		bbnote "WARNING: applying allow-nopw.patch which allows password-less logins!"
		patch -p1 < ${WORKDIR}/allow-nopw.patch
	fi
}

addtask do_debug_patch after do_patch before do_configure

do_install() {
	install -d ${D}${sysconfdir} \
		   ${D}${sysconfdir}/init.d \
		   ${D}${sysconfdir}/default \
		   ${D}${sysconfdir}/dropbear \
                   ${D}${bindir} \
		   ${D}${sbindir} \
		   ${D}${localstatedir}

	install -m 0755 dropbearmulti ${D}${sbindir}/
	ln -s ${sbindir}/dropbearmulti ${D}${bindir}/dbclient
	
	for i in ${SBINCOMMANDS}
	do
		ln -s ./dropbearmulti ${D}${sbindir}/$i
	done
	cat ${WORKDIR}/init | sed -e 's,/etc,${sysconfdir},g' \
				  -e 's,/usr/sbin,${sbindir},g' \
				  -e 's,/var,${localstatedir},g' \
				  -e 's,/usr/bin,${bindir},g' \
				  -e 's,/usr,${prefix},g' > ${D}${sysconfdir}/init.d/dropbear
	chmod 755 ${D}${sysconfdir}/init.d/dropbear
}

pkg_postinst_${PN} () {
	update-alternatives --install ${bindir}/scp scp ${sbindir}/dropbearmulti 20
	update-alternatives --install ${bindir}/ssh ssh ${sbindir}/dropbearmulti 20
}

pkg_postrm_append_${PN} () {
  if [ -f "${sysconfdir}/dropbear/dropbear_rsa_host_key" ]; then
        rm ${sysconfdir}/dropbear/dropbear_rsa_host_key
  fi
  if [ -f "${sysconfdir}/dropbear/dropbear_dss_host_key" ]; then
        rm ${sysconfdir}/dropbear/dropbear_dss_host_key
  fi
  update-alternatives --remove ssh ${bindir}/dropbearmulti
  update-alternatives --remove scp ${bindir}/dropbearmulti
}
