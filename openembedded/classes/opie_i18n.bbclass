# classes/opie_i18n.oeclass 		Matthias 'CoreDump' Hentges			16-10-2004          
#
# Automatically builds i18n ipks for opie packages. It downloads opie-i18n from opie CVS
# and tries to guess the name of the .ts file based on the package name:
# ${PN}.ts, lib${PN}.ts and opie-${PN}.ts are all valid. The .ts "guessing" can be
# disabled by setting I18N_FILES in the .oe file.
#
# Todo:
#

I18N_STATS = "1"
SRC_URI += "${HANDHELDS_CVS};module=opie/i18n" 
DEPENDS += "opie-i18n"
	
die () {
	echo -e "opie_18n: ERROR: $1"
	exit 1
}	

python do_build_opie_i18n_data() {

	import os, bb, re
	workdir = bb.data.getVar("WORKDIR", d, 1)
	packages = bb.data.getVar("PACKAGES", d, 1)
	files = bb.data.getVar("FILES", d, 1)
	section = bb.data.getVar("SECTION", d, 1)
	pn = bb.data.getVar("PN", d, 1)
	rdepends =  bb.data.getVar("RDEPENDS", d, 1)
	
	if os.path.exists(workdir + "/PACKAGES.tmp"):
		fd = open(workdir + "/PACKAGES.tmp", 'r')
		lines = fd.readlines()
		fd.close()
	
		bb.data.setVar('PACKAGES', " ".join(lines).lower() + " " + packages, d)	

		fd = open(workdir + "/FILES.tmp", 'r')
		lines = fd.readlines()
		fd.close()

		for l in lines:			
			x = re.split("\#", l)
			bb.data.setVar('FILES_%s' % x[0].lower(), " " + x[1].strip('\n'), d)
			bb.data.setVar('SECTION_%s' % x[0].lower(), "opie/translations", d)
			bb.data.setVar('RDEPENDS_%s' % x[0].lower(), pn, d)

		bb.data.setVar('SECTION_%s' % pn, section, d)
		bb.data.setVar('RDEPENDS', rdepends, d)
	else:
		bb.note("No translations found for package " + pn)
}
	
do_build_opie_i18n () {

	cd "${WORKDIR}/i18n" || die "ERROR:\nCouldn't find Opies i18n sources in ${PN}/i18n\nMake sure that <inherit opie_i18n> or <inherit opie> is *below* <SRC_URIS =>!"
	
	if test -z "${I18N_FILES}"
	then
		package_name="`echo "${PN}"| sed "s/^opie\-//"`"
		package_name2="`echo "${PN}"| sed "s/^opie\-//;s/\-//"`"
		test "$package_name" != "$package_name2" && I18N_FILES="${package_name}.ts lib${package_name}.ts opie-${package_name}.ts ${package_name2}.ts lib${package_name2}.ts opie-${package_name2}.ts"
		test "$package_name" = "$package_name2" && I18N_FILES="${package_name}.ts lib${package_name}.ts opie-${package_name}.ts"
		echo -e "I18N Datafiles: ${I18N_FILES} (auto-detected)\nYou can overide the auto-detection by setting I18N_FILES in your .oe file"
	else
		echo "I18N Datafiles: ${I18N_FILES} (provided by .bb)"
	fi	
	
	rm -f "${WORKDIR}/FILES.tmp" "${WORKDIR}/PACKAGES.tmp"
	
	echo -e "\nFILES is set to [${FILES}]\n"
	
	for file in ${I18N_FILES}
	do
		echo "Working on [$file]"
		for ts_file in `ls -1 */*.ts | egrep "/$file"`
		do
			echo -e "\tCompiling [$ts_file]"
			cd "${WORKDIR}/i18n/`dirname $ts_file`" || die "[${WORKDIR}/i18n/`dirname $ts_file`] not found"
			opie-lrelease "`basename $ts_file`" || die "lrelease failed! Make sure that <inherit opie_i18n> or <inherit opie> is *below* <DEPENDS =>!"							
			
			# $lang is the language as in de_DE, $lang_sane replaces "_" with "-"
			# to allow packaging as "_" is not allowed in a package name
			lang="`echo "$ts_file" | sed -n "s#\(.*\)/\(.*\)#\1#p"`"
			lang_sane="`echo "$ts_file" | sed -n "s#\(.*\)/\(.*\)#\1#p"|sed s/\_/\-/`"
			echo -e "\tPackaging [`basename $ts_file`] for language [$lang]"
			
			install -d ${D}${palmtopdir}/i18n/$lang			
			install -m 0644 ${WORKDIR}/i18n/$lang/.directory ${D}${palmtopdir}/i18n/$lang/
			install -m 0644 ${WORKDIR}/i18n/$lang/*.qm "${D}${palmtopdir}/i18n/$lang/"
						
			# As it is not possible to modify OE vars from within a _shell_ function,
			# some major hacking was needed. These two files will be read by the python 
			# function do_build_opie_i18n_data() which sets the variables FILES_* and
			# PACKAGES as needed. 
			echo -n "${PN}-${lang_sane} " >> "${WORKDIR}/PACKAGES.tmp"						
			echo -e "${PN}-${lang_sane}#${palmtopdir}/i18n/$lang" >> "${WORKDIR}/FILES.tmp"								
			
			ts_found_something=1
		done
		
		if test "$ts_found_something" != 1
		then
			echo -e "\tNo translations found"
		else
			ts_found_something=""
			ts_found="$ts_found $file"
		fi
				
		# Only used for debugging purposes
		test "${I18N_STATS}" = 1 && cd "${WORKDIR}/i18n"	

		echo -e "Completed [$file]\n\n"
	done	

	qt_dirs="apps  bin  etc  lib  pics  plugins  share  sounds"

	for dir in $qt_dirs
	do
		dir_="$dir_ ${palmtopdir}/$dir "
	done

	
	# If we don't adjust FILES to exclude the i18n directory, we will end up with
	# _lots_ of empty i18n/$lang directories in the original .ipk.	
	if (echo "${FILES}" | egrep "${palmtopdir}/? |${palmtopdir}/?$") &>/dev/null
	then
		echo "NOTE: FILES was set to ${palmtopdir} which would include the i18n directory"
		echo -e "\n\nI'll remove ${palmtopdir} from FILES and replace it with all directories"
		echo "below QtPalmtop, except i18n ($qt_dirs). See classes/opie_i18n.oeclass for details"

		# Removes /opt/QtPalmtop from FILES but keeps /opt/QtPalmtop/$some_dir
		FILES="`echo "$FILES"| sed "s#${palmtopdir}[/]\?\$\|${palmtopdir}[/]\? ##"`"

		echo "${PN}#$FILES $dir_" >> "${WORKDIR}/FILES.tmp"
	fi

	# This is the common case for OPIE apps which are installed by opie.oeclass magic
	if test -z "${FILES}"
	then
		echo "NOTE:"
		echo -e "Since FILES is empty, i'll add all directories below ${palmtopdir} to it,\nexcluding i18n: ( $qt_dirs )"
		echo "${PN}#$FILES $dir_" >> "${WORKDIR}/FILES.tmp"
	fi	
	
	if ! test -e "${WORKDIR}/PACKAGES.tmp" -a "${I18N_STATS}" = 1
	then
		echo "No translations for package [${PN}]" >> /tmp/oe-i18n-missing.log
	else
		echo "Using [$ts_found ] for package [${PN}]" >> /tmp/oe-i18n.log
	fi
	
	# While this might not be very elegant, it safes a _ton_ of space (~30Mb) for
	# each opie package.
	for file in $(ls */*.ts | egrep -v "`echo "$ts_found"| sed "s/^\ //;s/\ /\|/"`")
	do
		rm "$file"
	done
	
	return 0
}

addtask build_opie_i18n before do_compile
addtask build_opie_i18n_data after do_build_opie_i18n before do_compile
