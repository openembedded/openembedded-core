DESCRIPTION = "Ncurses library"
HOMEPAGE = "http://www.gnu.org/software/ncurses/ncurses.html"
LICENSE = "MIT"
SECTION = "libs"
DEPENDS = "ncurses-native"
PACKAGES_prepend = "ncurses-tools "
PACKAGES_append = " ncurses-terminfo"
FILES_ncurses_append = " ${datadir}/tabset"
RSUGGESTS_${PN} = "ncurses-terminfo"

inherit autotools

# This keeps only tput/tset in ncurses
# clear/reset are in already busybox
FILES_ncurses-tools = "${bindir}/tic ${bindir}/toe ${bindir}/infotocap ${bindir}/captoinfo ${bindir}/infocmp ${bindir}/clear.${PN} ${bindir}/reset.${PN} ${bindir}/tack "
FILES_ncurses-terminfo = "${datadir}/terminfo"
FILES_${PN} = "${bindir}/tput ${bindir}/tset ${libdir}/lib*.so.* usr/share/tabset etc/terminfo"

PARALLEL_MAKE=""

EXTRA_OECONF = "--with-shared \
	        --without-profile \
	        --without-debug \
	        --disable-rpath \
	        --enable-echo \
	        --enable-const \
	        --without-ada \
	        --enable-termcap \
	        --without-cxx-binding \
	        --with-terminfo-dirs=${sysconfdir}/terminfo:${datadir}/terminfo \
	        --enable-overwrite"
export BUILD_CCFLAGS = "-I${S}/ncurses -I${S}/include ${BUILD_CFLAGS}"
export BUILD_LDFLAGS = ""
export EXTRA_OEMAKE = '"BUILD_LDFLAGS=" "BUILD_CCFLAGS=${BUILD_CCFLAGS}"'

do_stage () {
	for i in libncurses libpanel libform libmenu; do
		oe_libinstall -so -a -C lib $i ${STAGING_LIBDIR}
	done
	for h in ncurses_*.h curses.h eti.h form.h menu.h panel.h \
		termcap.h term.h unctrl.h; do
		       install -m 0644 include/$h ${STAGING_INCDIR}/
	done
	ln -sf curses.h ${STAGING_INCDIR}/ncurses.h
	ln -sf libncurses.so ${STAGING_LIBDIR}/libtermcap.so
	ln -sf libncurses.a ${STAGING_LIBDIR}/libtermcap.a
}

do_install() {
	autotools_do_install

	# our ncurses has termcap support
	ln -sf libncurses.so ${D}${libdir}/libtermcap.so
	ln -sf libncurses.a ${D}${libdir}/libtermcap.a

	# include some basic terminfo files
	# stolen ;) from gentoo and modified a bit
	for x in ansi console dumb linux rxvt screen sun vt{52,100,102,200,220} xterm-color xterm-xfree86
        do
                local termfile="$(find "${D}${datadir}/terminfo/" -name "${x}" 2>/dev/null)"
                local basedir="$(basename $(dirname "${termfile}"))"

                if [ -n "${termfile}" ]
                then
                        install -d ${D}${sysconfdir}/terminfo/${basedir}
                        mv ${termfile} ${D}${sysconfdir}/terminfo/${basedir}/
                        ln -s /etc/terminfo/${basedir}/${x} \
                                ${D}${datadir}/terminfo/${basedir}/${x}
                fi
        done
	# i think we can use xterm-color as default xterm
	if [ -e ${D}${sysconfdir}/terminfo/x/xterm-color ]
	then
		ln -sf xterm-color ${D}${sysconfdir}/terminfo/x/xterm
	fi

	mv ${D}${bindir}/clear ${D}${bindir}/clear.${PN}
	mv ${D}${bindir}/reset ${D}${bindir}/reset.${PN}
}
		
		
pkg_postinst_ncurses-tools () {
	update-alternatives --install ${bindir}/clear clear clear.${PN} 100
	update-alternatives --install ${bindir}/reset reset reset.${PN} 100
}
				
			
pkg_prerm_ncurses-tools () {
	update-alternatives --remove clear clear.${PN}
	update-alternatives --remove reset reset.${PN}
}
