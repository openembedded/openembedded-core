DESCRIPTION = "Inittab for sysvinit"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PR = "r8"

SRC_URI = "file://inittab"

S = "${WORKDIR}/sysvinit-${PV}"

INHIBIT_DEFAULT_DEPS = "1"

do_compile() {
	:
}

do_install() {
	install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/inittab ${D}${sysconfdir}/inittab
    if [ ! -z "${SERIAL_CONSOLE}" ]; then
        echo "S:2345:respawn:${base_sbindir}/getty ${SERIAL_CONSOLE}" >> ${D}${sysconfdir}/inittab
    fi

    idx=0
    tmp="${SERIAL_CONSOLES}"
    for i in $tmp
    do
	j=`echo ${i} | sed s/\;/\ /g`
	echo "${idx}:12345:respawn:${base_sbindir}/getty ${j}" >> ${D}${sysconfdir}/inittab

	idx=`expr $idx + 1`
    done

    if [ "${USE_VT}" = "1" ]; then
        cat <<EOF >>${D}${sysconfdir}/inittab
# ${base_sbindir}/getty invocations for the runlevels.
#
# The "id" field MUST be the same as the last
# characters of the device (after "tty").
#
# Format:
#  <id>:<runlevels>:<action>:<process>
#

EOF

        for n in ${SYSVINIT_ENABLED_GETTYS}
        do
            echo "$n:2345:respawn:${base_sbindir}/getty 38400 tty$n" >> ${D}${sysconfdir}/inittab
        done
        echo "" >> ${D}${sysconfdir}/inittab
    fi
}

pkg_postinst_${PN} () {
# run this on the target
if [ "x$D" = "x" ]; then
	tmp="${SERIAL_CONSOLES_CHECK}"
	for i in $tmp
	do
		j=`echo ${i} | sed s/^.*\;//g`
		if [ -z "`cat /proc/consoles | grep ${j}`" ]; then
			sed -i /^.*${j}$/d /etc/inittab
		fi
	done
	kill -HUP 1
else
	if [ "${SERIAL_CONSOLES_CHECK}" = "" ]; then
		exit 0
	else
		exit 1
	fi
fi
}

# USE_VT and SERIAL_CONSOLE are generally defined by the MACHINE .conf.
# Set PACKAGE_ARCH appropriately.
PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} = "${sysconfdir}/inittab"
CONFFILES_${PN} = "${sysconfdir}/inittab"

USE_VT ?= "1"
SYSVINIT_ENABLED_GETTYS ?= "1"



