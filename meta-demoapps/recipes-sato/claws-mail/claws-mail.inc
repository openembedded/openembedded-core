SECTION = "x11/network"
DESCRIPTION = "Mail user agent"
#DEPENDS = "gtk+ gpgme libetpan libgnomeprint aspell openssl"
DEPENDS = "gtk+ libetpan openssl libowl"
LICENSE = "GPL"
PR = "r7"

SRC_URI = "\
	${SOURCEFORGE_MIRROR}/sylpheed-claws/claws-mail-${PV}.tar.bz2	\
	file://desktop.patch;patch=1				\
	file://streamline-ui.patch;patch=1				\
	file://owl-window-menu.patch;patch=1				\
  file://fix-build.patch;patch=1    \
	"

FILES_${PN} = "${bindir} ${datadir}/pixmaps ${datadir}/applications"

#EXTRA_OECONF = "--disable-aspell-test \
# --disable-dillo-viewer-plugin --with-aspell-prefix=${STAGING_DIR_HOST}${prefix} \
# --enable-aspell"

EXTRA_OECONF = "--disable-aspell-test 		\
		--disable-aspell 		\
		--disable-manual		\
		--disable-crash-dialog		\
		--disable-jpilot		\
		--disable-trayicon-plugin	\
		--disable-spamassassin-plugin	\
		--disable-bogofilter-plugin	\
		--disable-pgpcore-plugin	\
		--disable-pgpmime-plugin	\
		--disable-pgpinline-plugin	\
  		--disable-dillo-viewer-plugin	\
		--disable-clamav-plugin		\
		--disable-gnomeprint		\
		--disable-valgrind		\
    --disable-static    \
		"

#  		--disable-openssl 		\
#		--disable-libetpan		\

CFLAGS += "-D_GNU_SOURCE"

inherit autotools pkgconfig gettext

do_install_append() {
	install -d ${D}${datadir}/applications
	install -m 0644 claws-mail.desktop ${D}${datadir}/applications/
	install -d ${D}${datadir}/pixmaps
	install -m 0644 claws-mail.png ${D}${datadir}/pixmaps/
}

#python populate_packages_prepend () {
#	abiword_libdir = bb.data.expand('${libdir}/claws-mail/plugins', d)
#
#	do_split_packages(d, abiword_libdir, '^(.*)\.so$', 'claws-mail-plugin-%s', 'Claws plugin for %s', extra_depends='')
#}
