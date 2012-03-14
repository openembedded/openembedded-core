DESCRIPTION = "Timezone data"
HOMEPAGE = "ftp://elsie.nci.nih.gov/pub/"
SECTION = "base"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://asia;beginline=2;endline=3;md5=06468c0e84ef4d4c97045a4a29b08234"
DEPENDS = "tzcode-native"

PR = "r0"

RCONFLICTS= "timezones timezone-africa timezone-america timezone-antarctica \
             timezone-arctic timezone-asia timezone-atlantic \
             timezone-australia timezone-europe timezone-indian \
             timezone-iso3166.tab timezone-pacific timezone-zone.tab"

SRC_URI = "ftp://ftp.iana.org/tz/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzdata.md5sum] = "0615fd29def380a917e528433c820368"
SRC_URI[tzdata.sha256sum] = "2f9f8e2d1ae087be5917f60c3946e8dc3fe1068d7738c3395f2125135309e745"

S = "${WORKDIR}"

DEFAULT_TIMEZONE ?= "Universal"

TZONES= "africa antarctica asia australasia europe northamerica southamerica  \
         factory solar87 solar88 solar89 etcetera backward systemv \
        "
# pacificnew 

do_compile () {
        for zone in ${TZONES}; do \
            ${STAGING_BINDIR_NATIVE}/zic -d ${WORKDIR}${datadir}/zoneinfo -L /dev/null \
                -y ${S}/yearistype.sh ${S}/${zone} ; \
            ${STAGING_BINDIR_NATIVE}/zic -d ${WORKDIR}${datadir}/zoneinfo/posix -L /dev/null \
                -y ${S}/yearistype.sh ${S}/${zone} ; \
            ${STAGING_BINDIR_NATIVE}/zic -d ${WORKDIR}${datadir}/zoneinfo/right -L ${S}/leapseconds \
                -y ${S}/yearistype.sh ${S}/${zone} ; \
        done
}

do_install () {
        install -d ${D}/$exec_prefix ${D}${datadir}/zoneinfo
        cp -pPR ${S}/$exec_prefix ${D}/
        # libc is removing zoneinfo files from package
        cp -pP "${S}/zone.tab" ${D}${datadir}/zoneinfo
        cp -pP "${S}/iso3166.tab" ${D}${datadir}/zoneinfo

        # Install default timezone
        install -d ${D}${sysconfdir}
        echo ${DEFAULT_TIMEZONE} > ${D}${sysconfdir}/timezone

        chown -R root:root ${D}
}

pkg_postinst_${PN} () {

# code taken from Gentoo's tzdata ebuild

	etc_lt="$D${sysconfdir}/localtime"
	src="$D${sysconfdir}/timezone"

	if [ -e ${src} ] ; then
		tz=$(sed -e 's:#.*::' -e 's:[[:space:]]*::g' -e '/^$/d' "${src}")
	else
		tz="FUBAR"
	fi
	
	if [ -z ${tz} ] ; then
		return 0
	fi
	
	if [ ${tz} = "FUBAR" ] ; then
		echo "You do not have TIMEZONE set in ${src}."

		if [ ! -e ${etc_lt} ] ; then
			# if /etc/localtime is a symlink somewhere, assume they
			# know what they're doing and they're managing it themselves
			if [ ! -L ${etc_lt} ] ; then
				cp -f "$D${datadir}/zoneinfo/Universal" "${etc_lt}"
				echo "Setting ${etc_lt} to Universal."
			else
				echo "Assuming your ${etc_lt} symlink is what you want; skipping update."
			fi
		else
			echo "Skipping auto-update of ${etc_lt}."
		fi
		return 0
	fi

	if [ ! -e "$D${datadir}/zoneinfo/${tz}" ] ; then
		echo "You have an invalid TIMEZONE setting in ${src}"
		echo "Your ${etc_lt} has been reset to Universal; enjoy!"
		tz="Universal"
	fi
	echo "Updating ${etc_lt} with $D${datadir}/zoneinfo/${tz}"
	if [ -L ${etc_lt} ] ; then
		rm -f "${etc_lt}"
	fi
	cp -f "$D${datadir}/zoneinfo/${tz}" "${etc_lt}"
}

# Packages primarily organized by directory with a major city
# in most time zones in the base package

PACKAGES = "tzdata tzdata-misc tzdata-posix tzdata-right tzdata-africa \
    tzdata-americas tzdata-antarctica tzdata-arctic tzdata-asia \
    tzdata-atlantic tzdata-australia tzdata-europe tzdata-pacific"

FILES_tzdata-africa += "${datadir}/zoneinfo/Africa/*"
RPROVIDES_tzdata-africa = "tzdata-africa"

FILES_tzdata-americas += "${datadir}/zoneinfo/America/*  \
                ${datadir}/zoneinfo/US/*                \
                ${datadir}/zoneinfo/Brazil/*            \
                ${datadir}/zoneinfo/Canada/*            \
                ${datadir}/zoneinfo/Mexico/*            \
                ${datadir}/zoneinfo/Chile/*"
RPROVIDES_tzdata-americas = "tzdata-americas"

FILES_tzdata-antarctica += "${datadir}/zoneinfo/Antarctica/*"
RPROVIDES_tzdata-antarctica = "tzdata-antarctica"

FILES_tzdata-arctic += "${datadir}/zoneinfo/Arctic/*"
RPROVIDES_tzdata-arctic = "tzdata-arctic"

FILES_tzdata-asia += "${datadir}/zoneinfo/Asia/*        \
                ${datadir}/zoneinfo/Indian/*            \
                ${datadir}/zoneinfo/Mideast/*"
RPROVIDES_tzdata-asia = "tzdata-asia"

FILES_tzdata-atlantic += "${datadir}/zoneinfo/Atlantic/*"
RPROVIDES_tzdata-atlantic = "tzdata-atlantic"

FILES_tzdata-australia += "${datadir}/zoneinfo/Australia/*"
RPROVIDES_tzdata-australia = "tzdata-australia"

FILES_tzdata-europe += "${datadir}/zoneinfo/Europe/*"
RPROVIDES_tzdata-europe = "tzdata-europe"

FILES_tzdata-pacific += "${datadir}/zoneinfo/Pacific/*"
RPROVIDES_tzdata-pacific = "tzdata-pacific"

FILES_tzdata-posix += "${datadir}/zoneinfo/posix/*"
RPROVIDES_tzdata-posix = "tzdata-posix"

FILES_tzdata-right += "${datadir}/zoneinfo/right/*"
RPROVIDES_tzdata-right = "tzdata-right"


FILES_tzdata-misc += "${datadir}/zoneinfo/Cuba           \
                ${datadir}/zoneinfo/Egypt                \
                ${datadir}/zoneinfo/Eire                 \
                ${datadir}/zoneinfo/Factory              \
                ${datadir}/zoneinfo/GB-Eire              \
                ${datadir}/zoneinfo/Hongkong             \
                ${datadir}/zoneinfo/Iceland              \
                ${datadir}/zoneinfo/Iran                 \
                ${datadir}/zoneinfo/Israel               \
                ${datadir}/zoneinfo/Jamaica              \
                ${datadir}/zoneinfo/Japan                \
                ${datadir}/zoneinfo/Kwajalein            \
                ${datadir}/zoneinfo/Libya                \
                ${datadir}/zoneinfo/Navajo               \
                ${datadir}/zoneinfo/Poland               \
                ${datadir}/zoneinfo/Portugal             \
                ${datadir}/zoneinfo/Singapore            \
                ${datadir}/zoneinfo/Turkey"
RPROVIDES_tzdata-misc = "tzdata-misc"


FILES_${PN} += "${datadir}/zoneinfo/Pacific/Honolulu     \
                ${datadir}/zoneinfo/America/Anchorage    \
                ${datadir}/zoneinfo/America/Los_Angeles  \
                ${datadir}/zoneinfo/America/Denver       \
                ${datadir}/zoneinfo/America/Chicago      \
                ${datadir}/zoneinfo/America/New_York     \
                ${datadir}/zoneinfo/America/Caracas      \
                ${datadir}/zoneinfo/America/Sao_Paulo    \
                ${datadir}/zoneinfo/Europe/London        \
                ${datadir}/zoneinfo/Europe/Paris         \
                ${datadir}/zoneinfo/Africa/Cairo         \
                ${datadir}/zoneinfo/Europe/Moscow        \
                ${datadir}/zoneinfo/Asia/Dubai           \
                ${datadir}/zoneinfo/Asia/Karachi         \
                ${datadir}/zoneinfo/Asia/Dhaka           \
                ${datadir}/zoneinfo/Asia/Bankok          \
                ${datadir}/zoneinfo/Asia/Hong_Kong       \
                ${datadir}/zoneinfo/Asia/Tokyo           \
                ${datadir}/zoneinfo/Australia/Darwin     \
                ${datadir}/zoneinfo/Australia/Adelaide   \
                ${datadir}/zoneinfo/Australia/Brisbane   \
                ${datadir}/zoneinfo/Australia/Sydney     \
                ${datadir}/zoneinfo/Pacific/Noumea       \
                ${datadir}/zoneinfo/CET                  \
                ${datadir}/zoneinfo/CST6CDT              \
                ${datadir}/zoneinfo/EET                  \
                ${datadir}/zoneinfo/EST                  \
                ${datadir}/zoneinfo/EST5EDT              \
                ${datadir}/zoneinfo/GB                   \
                ${datadir}/zoneinfo/GMT                  \
                ${datadir}/zoneinfo/GMT+0                \
                ${datadir}/zoneinfo/GMT-0                \
                ${datadir}/zoneinfo/GMT0                 \
                ${datadir}/zoneinfo/Greenwich            \
                ${datadir}/zoneinfo/HST                  \
                ${datadir}/zoneinfo/MET                  \
                ${datadir}/zoneinfo/MST                  \
                ${datadir}/zoneinfo/MST7MDT              \
                ${datadir}/zoneinfo/NZ                   \
                ${datadir}/zoneinfo/NZ-CHAT              \
                ${datadir}/zoneinfo/PRC                  \
                ${datadir}/zoneinfo/PST8PDT              \
                ${datadir}/zoneinfo/ROC                  \
                ${datadir}/zoneinfo/ROK                  \
                ${datadir}/zoneinfo/UCT                  \
                ${datadir}/zoneinfo/UTC                  \
                ${datadir}/zoneinfo/Universal            \
                ${datadir}/zoneinfo/W-SU                 \
                ${datadir}/zoneinfo/WET                  \
                ${datadir}/zoneinfo/Zulu                 \
                ${datadir}/zoneinfo/zone.tab             \
                ${datadir}/zoneinfo/iso3166.tab          \
                ${datadir}/zoneinfo/Etc/*"
