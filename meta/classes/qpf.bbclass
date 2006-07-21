PACKAGE_ARCH = "all"

do_configure() {
	:
}

do_compile() {
	:
}

pkg_postinst_fonts() {
#!/bin/sh
set -e
. /etc/profile
${sbindir}/update-qtfontdir
}

pkg_postrm_fonts() {
#!/bin/sh
set -e
. /etc/profile
${sbindir}/update-qtfontdir -f
}

python populate_packages_prepend() {
	postinst = bb.data.getVar('pkg_postinst_fonts', d, 1)
	postrm   = bb.data.getVar('pkg_postrm_fonts', d, 1)
	fontdir  = bb.data.getVar('palmtopdir', d, 1) + '/lib/fonts'
	pkgregex = "^([a-z-]*_[0-9]*).*.qpf$"
	pkgpattern = bb.data.getVar('QPF_PKGPATTERN', d, 1) or 'qpf-%s'
	pkgdescription = bb.data.getVar('QPF_DESCRIPTION', d, 1) or 'QPF font %s'

        do_split_packages(d, root=fontdir, file_regex=pkgregex, output_pattern=pkgpattern, 
			  description=pkgdescription, postinst=postinst, postrm=postrm, recursive=True, hook=None, 
			  extra_depends='qpf-font-common')
}
