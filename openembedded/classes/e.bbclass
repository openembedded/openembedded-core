MAINTAINER = "Justin Patrin <papercrane@reversefold.com>"
HOMEPAGE = "http://www.enlightenment.org"
SECTION = "e/apps"

inherit autotools pkgconfig binconfig

do_prepsources () {
  make clean distclean || true
}
addtask prepsources after do_fetch before do_unpack

def binconfig_suffix(d):
	import bb
        return ["","-native"][bb.data.inherits_class('native', d)]

export CURL_CONFIG		= "${STAGING_BINDIR}/curl-config${@binconfig_suffix(d)}"
export EDB_CONFIG		= "${STAGING_BINDIR}/edb-config${@binconfig_suffix(d)}"
export EET_CONFIG		= "${STAGING_BINDIR}/eet-config${@binconfig_suffix(d)}"
export EVAS_CONFIG		= "${STAGING_BINDIR}/evas-config${@binconfig_suffix(d)}"
export ECORE_CONFIG		= "${STAGING_BINDIR}/ecore-config${@binconfig_suffix(d)}"
export EMBRYO_CONFIG		= "${STAGING_BINDIR}/embryo-config${@binconfig_suffix(d)}"
export ENGRAVE_CONFIG		= "${STAGING_BINDIR}/engrave-config${@binconfig_suffix(d)}"
export ENLIGHTENMENT_CONFIG	= "${STAGING_BINDIR}/enlightenment-config${@binconfig_suffix(d)}"
export EPSILON_CONFIG		= "${STAGING_BINDIR}/epsilon-config${@binconfig_suffix(d)}"
export EPEG_CONFIG		= "${STAGING_BINDIR}/epeg-config${@binconfig_suffix(d)}"
export ESMART_CONFIG		= "${STAGING_BINDIR}/esmart-config${@binconfig_suffix(d)}"
export FREETYPE_CONFIG		= "${STAGING_BINDIR}/freetype-config${@binconfig_suffix(d)}"
export IMLIB2_CONFIG		= "${STAGING_BINDIR}/imlib2-config${@binconfig_suffix(d)}"

do_compile_prepend() {
	find ${S} -name Makefile | xargs sed -i 's:/usr/include:${STAGING_INCDIR}:'
	find ${S} -name Makefile | xargs sed -i 's:/usr/X11R6/include:${STAGING_INCDIR}:'
}

PACKAGES = "${PN} ${PN}-themes"
FILES_${PN} = "${libdir}/lib*.so*"
FILES_${PN}-themes = "${datadir}/${PN}/themes ${datadir}/${PN}/data ${datadir}/${PN}/fonts ${datadir}/${PN}/pointers ${datadir}/${PN}/images ${datadir}/${PN}/users ${datadir}/${PN}/images ${datadir}/${PN}/styles"
