addtask lint before do_fetch
do_lint[nostamp] = "1"
python do_lint() {
	def testVar(var, explain=None):
		try:
			s = d[var]
			return s["content"]
		except KeyError:
			bb.error("%s is not set" % var)
			if explain: bb.note(explain)
			return None


	##############################
	# Test that DESCRIPTION exists
	#
	testVar("DESCRIPTION")


	##############################
	# Test that HOMEPAGE exists
	#
	s = testVar("HOMEPAGE")
	if s=="unknown":
		bb.error("HOMEPAGE is not set")
	elif not s.startswith("http://"):
		bb.error("HOMEPAGE doesn't start with http://")



	##############################
	# Test for valid LICENSE
	#
	valid_licenses = {
		"GPL-2"		: "GPLv2",
		"GPL LGPL FDL"	: True,
		"GPL PSF"	: True,
		"GPL/QPL"	: True,
		"GPL"		: True,
		"GPLv2"		: True,
		"IBM"		: True,
		"LGPL GPL"	: True,
		"LGPL"		: True,
		"MIT"		: True,
		"OSL"		: True,
		"Perl"		: True,
		"Public Domain"	: True,
		"QPL"		: "GPL/QPL",
		}
	s = testVar("LICENSE")
	if s=="unknown":
		bb.error("LICENSE is not set")
	elif s.startswith("Vendor"):
		pass
	else:
		try:
			newlic = valid_licenses[s]
			if newlic == False:
				bb.note("LICENSE '%s' is not recommended" % s)
			elif newlic != True:
				bb.note("LICENSE '%s' is not recommended, better use '%s'" % (s, newsect))
		except:
			bb.note("LICENSE '%s' is not recommended" % s)


	##############################
	# Test for valid MAINTAINER
	#
	s = testVar("MAINTAINER")
	if s=="OpenEmbedded Team <openembedded-devel@openembedded.org>":
		bb.error("explicit MAINTAINER is missing, using default")
	elif s and s.find("@") == -1:
		bb.error("You forgot to put an e-mail address into MAINTAINER")


	##############################
	# Test for valid SECTION
	#
	# if Correct section: True	section name is valid
	#                     False	section name is invalid, no suggestion
	#                     string	section name is invalid, better name suggested
	#
	valid_sections = {
		# Current Section         Correct section
		"apps"			: True,
		"audio"			: True,
		"base"			: True,
		"console/games"		: True,
		"console/net"		: "console/network",
		"console/network"	: True,
		"console/utils"		: True,
		"devel"			: True,
		"developing"		: "devel",
		"devel/python"		: True,
		"fonts"			: True,
		"games"			: True,
		"games/libs"		: True,
		"gnome/base"		: True,
		"gnome/libs"		: True,
		"gpe"			: True,
		"gpe/libs"		: True,
		"gui"			: False,
		"libc"			: "libs",
		"libs"			: True,
		"libs/net"		: True,
		"multimedia"		: True,
		"net"			: "network",
		"NET"			: "network",
		"network"		: True,
		"opie/applets"		: True,
		"opie/applications"	: True,
		"opie/base"		: True,
		"opie/codecs"		: True,
		"opie/decorations"	: True,
		"opie/fontfactories"	: True,
		"opie/fonts"		: True,
		"opie/games"		: True,
		"opie/help"		: True,
		"opie/inputmethods"	: True,
		"opie/libs"		: True,
		"opie/multimedia"	: True,
		"opie/pim"		: True,
		"opie/setting"		: "opie/settings",
		"opie/settings"		: True,
		"opie/Shell"		: False,
		"opie/styles"		: True,
		"opie/today"		: True,
		"scientific"		: True,
		"utils"			: True,
		"x11"			: True,
		"x11/libs"		: True,
		"x11/wm"		: True,
		}
	s = testVar("SECTION")
	if s:
		try:
			newsect = valid_sections[s]
			if newsect == False:
				bb.note("SECTION '%s' is not recommended" % s)
			elif newsect != True:
				bb.note("SECTION '%s' is not recommended, better use '%s'" % (s, newsect))
		except:
			bb.note("SECTION '%s' is not recommended" % s)

		if not s.islower():
			bb.error("SECTION should only use lower case")



	
	##############################
	# Test for valid PRIORITY
	#
	valid_priorities = {
		"standard"		: True,
		"required"		: True,
		"optional"		: True,
		"extra"			: True,
		}
	s = testVar("PRIORITY")
	if s:
		try:
			newprio = valid_priorities[s]
			if newprio == False:
				bb.note("PRIORITY '%s' is not recommended" % s)
			elif newprio != True:
				bb.note("PRIORITY '%s' is not recommended, better use '%s'" % (s, newprio))
		except:
			bb.note("PRIORITY '%s' is not recommended" % s)

		if not s.islower():
			bb.error("PRIORITY should only use lower case")
	
}
