include conf/licenses.conf

SRC_DISTRIBUTECOMMAND[func] = "1"
python do_distribute_sources () {
	l = bb.data.createCopy(d)
	bb.data.update_data(l)
	licenses = (bb.data.getVar('LICENSE', d, 1) or "").split()
	if not licenses:
		bb.note("LICENSE not defined")
	src_distribute_licenses = (bb.data.getVar('SRC_DISTRIBUTE_LICENSES', d, 1) or "").split()
	# Explanation:
	# Space seperated items in LICENSE must *all* be distributable
	# Each space seperated item may be used under any number of | seperated licenses.
	# If any of those | seperated licenses are distributable, then that component is.
	# i.e. LICENSE = "GPL LGPL"
	#     In this case, both components are distributable.
	# LICENSE = "GPL|QPL|Proprietary"
	#     In this case, GPL is distributable, so the component is.
	valid = 1
	for l in licenses:
		lvalid = 0
		for i in l.split("|"):
			if i in src_distribute_licenses:
				lvalid = 1
		if lvalid != 1:
			valid = 0
	if valid == 0:
		bb.note("Licenses (%s) are not all listed in SRC_DISTRIBUTE_LICENSES, skipping source distribution" % licenses)
		return
	import re
	for s in (bb.data.getVar('A', d, 1) or "").split():
		s = re.sub(';.*$', '', s)
		cmd = bb.data.getVar('SRC_DISTRIBUTECOMMAND', d, 1)
		if not cmd:
			raise bb.build.FuncFailed("Unable to distribute sources, SRC_DISTRIBUTECOMMAND not defined")
		bb.data.setVar('SRC', s, d)
		bb.build.exec_func('SRC_DISTRIBUTECOMMAND', d)
}

addtask distribute_sources before do_build after do_fetch
