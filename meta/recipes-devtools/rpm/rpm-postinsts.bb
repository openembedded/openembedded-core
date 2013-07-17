DESCRIPTION = "RPM postinstall script"
SECTION = "core"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/LGPL-2.1;md5=1a6d268fd218675ffea8be556788b780"

inherit allarch
#
# Allow distributions to alter when [postponed] package install scripts are run
#
POSTINSTALL_INITPOSITION ?= "98"

do_fetch() {
	:
}

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	:
}

pkg_postinst_${PN} () {
if [ "x$D" != "x" ] && [ -f $D/var/lib/rpm/Packages ]; then
	install -d $D/${sysconfdir}/rcS.d
	cat > $D${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts << "EOF"
#!/bin/sh
[ -e ${sysconfdir}/default/postinst ] && . ${sysconfdir}/default/postinst
[ -d ${sysconfdir}/rpm-postinsts ] && for i in `ls ${sysconfdir}/rpm-postinsts/`; do
	i=${sysconfdir}/rpm-postinsts/$i
	echo "Running postinst $i..."
	if [ -x $i ]; then
		if [ "$POSTINST_LOGGING" = "1" ]; then
			$i >>$LOGFILE 2>&1
		else
			$i
		fi
		rm $i
	else
		echo "ERROR: postinst $i failed."
	fi
done
rm -f ${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts 2>/dev/null
EOF
	chmod 0755 $D${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts
fi
}

ALLOW_EMPTY_${PN} = "1"
