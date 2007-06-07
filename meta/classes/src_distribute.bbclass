SRC_DISTRIBUTECOMMAND[func] = "1"
python do_distribute_sources () {
	l = bb.data.createCopy(d)
	bb.data.update_data(l)
	licenses = (bb.data.getVar('LICENSE', d, 1) or "unknown").split()

	sources_dir = bb.data.getVar('SRC_DISTRIBUTEDIR', d, 1)
	import re
	for license in licenses:
		for entry in license.split("|"):
			for s in (bb.data.getVar('A', d, 1) or "").split():
				s = re.sub(';.*$', '', s)
				cmd = bb.data.getVar('SRC_DISTRIBUTECOMMAND', d, 1)
				if not cmd:
					raise bb.build.FuncFailed("Unable to distribute sources, SRC_DISTRIBUTECOMMAND not defined")
				bb.data.setVar('SRC', s, d)
				bb.data.setVar('SRC_DISTRIBUTEDIR', "%s/%s" % (sources_dir, entry), d)
				bb.build.exec_func('SRC_DISTRIBUTECOMMAND', d)
}

addtask distribute_sources before do_build after do_fetch

addtask distsrcall after do_distribute_sources
do_distall[recrdeptask] = "do_distribute_sources"
base_do_distsrcall() {
	:
}
