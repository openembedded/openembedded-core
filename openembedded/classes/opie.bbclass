#
# This oeclass takes care about some of the itchy details of installing parts
# of Opie applications. Depending on quicklaunch or not, plugin or not, the
# TARGET is either a shared object, a shared object with a link to quicklauncher,
# or a usual binary.
# 
# You have to provide two things: 1.) A proper SECTION field, and 2.) a proper APPNAME
# Then opie.oeclass will:
#    * create the directory for the binary and install the binary file(s)
#    * for applications: create the directory for the .desktop and install the .desktop file
#    * for quicklauncher applications: create the startup symlink to the quicklauncher
# You can override the automatic detection of APPTYPE, valid values are 'quicklaunch', 'binary', 'plugin'
# You can override the default location of APPDESKTOP (<workdir>/apps/<section>/)
#

inherit palmtop

# Note that when CVS changes to 1.2.2, the dash
# should be removed from OPIE_CVS_PV to convert
# to the standardised version format
OPIE_CVS_PV = "1.2.1+cvs-${SRCDATE}"

DEPENDS_prepend = "${@["libopie2 ", ""][(bb.data.getVar('PN', d, 1) == 'libopie2')]}"

# to be consistent, put all targets into workdir
EXTRA_QMAKEVARS_POST_append = " DESTDIR=${S}"

# Opie standard TAG value
TAG = "${@'v' + bb.data.getVar('PV',d,1).replace('.', '_')}"

# plan for later:
# add common scopes for opie applications, see qmake-native/common.pro
# qmake should care about all the details then. qmake can do that, i know it :)
#

python opie_do_opie_install() {
	import os, shutil
	section = bb.data.getVar( "SECTION", d ).split( '/' )[1] or "Applications"
	section = section.title()
	if section in ( "Base", "Libs" ):
		bb.note( "Section = Base or Libs. Target won't be installed automatically." )
		return

	#               SECTION         : BINDIR			DESKTOPDIR
	dirmap = { 	"Applets" 	: ( "/plugins/applets",		None 			),
			"Applications" 	: ( "<BINDIR>",			"/apps/Applications" 	),
			"Multimedia"	: ( "<BINDIR>",			"/apps/Applications"	),
			"Games"		: ( "<BINDIR>",			"/apps/Games"		),
			"Settings"	: ( "<BINDIR>",			"/apps/Settings"	),
			"Pim"		: ( "<BINDIR>",			"/apps/1Pim"		),
			"Examples"	: ( "<BINDIR>",			"/apps/Examples"	),
			"Shell"		: ( "/bin",			"/apps/Opie-SH"		),
			"Codecs"	: ( "/plugins/codecs",		None			),
			"Decorations"	: ( "/plugins/decorations",	None			),
			"Inputmethods"	: ( "/plugins/inputmethods",	None			),
			"Fontfactories" : ( "/plugins/fontfactories",	None			),
			"Security"	: ( "/plugins/security",	None			),
			"Styles"	: ( "/plugins/styles",		None			),
			"Today"		: ( "/plugins/today",		None			),
			"Datebook"	: ( "/plugins/holidays",	None			),
		"Networksettings"	: ( "/plugins/networksettings", None			) }

	if section not in dirmap:
		raise ValueError, "Unknown section '%s'. Valid sections are: %s" % ( section, dirmap.keys() )
	
	bindir, desktopdir = dirmap[section]
	APPNAME = bb.data.getVar( "APPNAME", d, True ) or bb.data.getVar( "PN", d, True )
	APPTYPE = bb.data.getVar( "APPTYPE", d, True )
	if not APPTYPE:
		if bindir == "<BINDIR>":
			APPTYPE = "quicklaunch"
		else:
			APPTYPE = "plugin"

	appmap = { "binary":"/bin", "quicklaunch":"/plugins/application" }
	if bindir == "<BINDIR>": bindir = appmap[APPTYPE]
	
	bb.note( "Section='%s', bindir='%s', desktopdir='%s', name='%s', type='%s'" %
               ( section, bindir, desktopdir, APPNAME, APPTYPE ) )

	S = bb.data.getVar( "S", d, 1 )
	D = "%s/image" % bb.data.getVar( "WORKDIR", d, True )
	WORKDIR = bb.data.getVar( "WORKDIR", d, True )
	palmtopdir = bb.data.getVar( "palmtopdir", d )
	APPDESKTOP = bb.data.getVar( "APPDESKTOP", d, True ) or "%s/%s" % ( WORKDIR, desktopdir )

	if desktopdir is not None:
		os.system( "install -d %s%s%s/" % ( D, palmtopdir, desktopdir ) )
		os.system( "install -m 0644 %s/%s.desktop %s%s%s/" % ( APPDESKTOP, APPNAME, D, palmtopdir, desktopdir ) )

	os.system( "install -d %s%s%s/" % ( D, palmtopdir, bindir ) )

	if APPTYPE == "binary":
		os.system( "install -m 0755 %s/%s %s%s%s/" % ( S, APPNAME, D, palmtopdir, bindir ) )
	elif APPTYPE == "quicklaunch":
		os.system( "install -m 0755 %s/lib%s.so %s%s%s/" % ( S, APPNAME, D, palmtopdir, bindir ) )
		os.system( "install -d %s%s/bin/" % ( D, palmtopdir ) )
		os.system( "ln -sf %s/bin/quicklauncher %s%s/bin/%s" % ( palmtopdir, D, palmtopdir, APPNAME ) )
	elif APPTYPE == "plugin":
		os.system( "install -m 0755 %s/lib%s.so %s%s%s/" % ( S, APPNAME, D, palmtopdir, bindir ) )
}

EXPORT_FUNCTIONS do_opie_install
addtask opie_install after do_compile before do_populate_staging
