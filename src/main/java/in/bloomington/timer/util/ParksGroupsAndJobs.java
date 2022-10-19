


class ParksGroupsAndJobs{
		//
		// find all Parks groups
		// Parks Dept ID = 5
		/**
			 select g.name 'Group Name' from groups g where g.department_id=5;
			 
 Group Name                          |
+------------------------------------+
| Parks - Admin 1                    |
| Parks - Admin 2                    |
| Parks - Admin 3                    |
| Parks - Operations 1               |
| Parks - Operations 2 (Adam Street) |
| Parks - Operations 3               |
| Parks - Recreation 1               |
| Parks - Recreation 2               |
| Parks - Recreation 3               |
| Parks - Recreation 4               |
| Parks - Recreation 5               |
| Parks - Sports 1                   |
| Parks - Sports 2                   |
| Parks - Sports 3                   |
| Parks - SYP Seasonal               |
| Parks - TLRC VOLLEY                |
| Parks - TLRC BYB                   |
| Parks - TLRC CONC/FAC              |
| Parks - TLRC                       |
| Parks - TLRC FRONT                 |
| Parks - AJB                        |
| Parks - Banneker                   |
| Parks - Community Events           |
| Parks - FSC Front                  |
| Parks - FSC Hockey                 |
| Parks - FSC Ice                    |
| Parks - Golf Maint                 |
| Parks - Golf Pro Shop              |
| Parks - Golf Union                 |
| Parks - Health & Wellness          |
| Parks - Land/Cem                   |
| Parks - Natural Resource/Trail     |
| Parks - Natural Resources          |
| Parks - OLC/WIN                    |
| Parks - OPS Union                  |
| Parks - OPS/Admin                  |
| Parks - OPS/Urban Forestry         |
| Parks - TLSP/SYP Union             |
| Parks - Sports Union Win/FSC       |
| Parks - TLSP                       |
| Parks - Mills Pool                 |
| Parks - Bryan Pool                 |
| Parks - SYP Landscape              |
-------------------------------------
Total 43

 Parks Groups that have temp employees
 ===============================
	 select distinct g.name 'Group Name' from jobs j join groups g on g.id = j.group_id join positions p on p.id = j.position_id where g.department_id=5 and j.salary_group_id=3 order by 1;

+------------------------------------+
| Group Name                         |
+------------------------------------+
| Parks - AJB                        |
| Parks - Banneker                   |
| Parks - Bryan Pool                 |
| Parks - Community Events           |
| Parks - FSC Front                  |
| Parks - FSC Hockey                 |
| Parks - FSC Ice                    |
| Parks - Golf Maint                 |
| Parks - Golf Pro Shop              |
| Parks - Health & Wellness          |
| Parks - Land/Cem                   |
| Parks - Mills Pool                 |
| Parks - Natural Resource/Trail     |
| Parks - Natural Resources          |
| Parks - Operations 1               |
| Parks - Operations 2 (Adam Street) |
| Parks - Operations 3               |
| Parks - OPS Union                  |
| Parks - OPS/Admin                  |
| Parks - OPS/Urban Forestry         |
| Parks - SYP Landscape              |
| Parks - SYP Seasonal               |
| Parks - TLRC                       |
| Parks - TLRC BYB                   |
| Parks - TLRC CONC/FAC              |
| Parks - TLRC FRONT                 |
| Parks - TLRC VOLLEY                |
| Parks - TLSP                       |
+------------------------------------+
28

		 */

		// find all job titles (positions) that are used by Parks temp employees
		/**
			 select distinct p.name 'Job Title',g.name 'Group Name' from jobs j join groups g on g.id = j.group_id join positions p on p.id = j.position_id where g.department_id=5 and j.salary_group_id=3 order by 2,1;

+-------------------------+------------------------------------+
| Job Title               | Group Name                         |
+-------------------------+------------------------------------+
| AJB-ATT-KC-NR           | Parks - AJB                        |
| AJB-ATTENDANT-KC        | Parks - AJB                        |
| AJB-LEAD KC-NR          | Parks - AJB                        |
| AJB-LEAD-801            | Parks - AJB                        |
| AJB-LEAD-KC-GF          | Parks - AJB                        |
| AJB-LEAD-KC-NR          | Parks - AJB                        |
| AJB-SPEC-KC-NR          | Parks - AJB                        |
| AJB-ST ASST-KC-GF       | Parks - AJB                        |
| AJB-ST ASST-KC-NR       | Parks - AJB                        |
| AJB-STF ASST KC-NR      | Parks - AJB                        |
| AJB-STF-ASST-NR         | Parks - AJB                        |
| AJB-STFASST-KC-GF       | Parks - AJB                        |
| AJB-STFASST-KC-NR       | Parks - AJB                        |
| AJB-SUP KC-NR           | Parks - AJB                        |
| AJB-SUP-KC              | Parks - AJB                        |
| AJB-SUP-KC-GF           | Parks - AJB                        |
| AJB-SUP-KC-NR           | Parks - AJB                        |
| AJB-SUP-KC-NR 4500      | Parks - AJB                        |
| AJB-SUP-KC-NR-4500      | Parks - AJB                        |
| AJB-SUP-KC-NR-4501      | Parks - AJB                        |
| AJB-SUP-NR-4500         | Parks - AJB                        |
| BBCC-BLD-SUP-NR         | Parks - Banneker                   |
| BBCC-LEAD-GF            | Parks - Banneker                   |
| BBCC-LEAD-GRANT-NR      | Parks - Banneker                   |
| BBCC-REC-LEAD-GF        | Parks - Banneker                   |
| BBCC-SPEC-GF            | Parks - Banneker                   |
| BBCC-SPEC-GRANT-NR      | Parks - Banneker                   |
| BBCC-SUP-GF             | Parks - Banneker                   |
| BBCC-SUP-NR             | Parks - Banneker                   |
| REC-LEAD-GF             | Parks - Banneker                   |
| BB-ATT-GF               | Parks - Bryan Pool                 |
| BP-ATT-GF               | Parks - Bryan Pool                 |
| BP-GUARD-GF             | Parks - Bryan Pool                 |
| BP-LEAD-GF              | Parks - Bryan Pool                 |
| BP-STF AST-GF           | Parks - Bryan Pool                 |
| MP-ATT-GF               | Parks - Bryan Pool                 |
| CE-LEAD-GF              | Parks - Community Events           |
| CE-LEAD-NR              | Parks - Community Events           |
| CE-SPEC-FM-NR           | Parks - Community Events           |
| CE-SPEC-GF              | Parks - Community Events           |
| CE-SPEC-NR              | Parks - Community Events           |
| CE-SUP-GF               | Parks - Community Events           |
| CE-SUP-NR               | Parks - Community Events           |
| SPEC-HEAD TECH-GF       | Parks - Community Events           |
| SPEC-HEAD TECH-NR       | Parks - Community Events           |
| FSC-ATT-GF              | Parks - FSC Front                  |
| FSC-LABII-GF            | Parks - FSC Front                  |
| FSC-SPEC-GF             | Parks - FSC Front                  |
| FSC-SPEC-NR             | Parks - FSC Front                  |
| FSC-SUP-GF              | Parks - FSC Front                  |
| FSC-HOCKEY-SPEC-NR      | Parks - FSC Hockey                 |
| HOCKEY-SPEC-NR          | Parks - FSC Hockey                 |
| FSC-LAB II-GF           | Parks - FSC Ice                    |
| FSC-LABII-GF            | Parks - FSC Ice                    |
| FSC-SPEC-INSTR-NR       | Parks - FSC Ice                    |
| FSC-SPEC-NR             | Parks - FSC Ice                    |
| SPEC-FSC- INSTR-NR      | Parks - FSC Ice                    |
| SPEC-FSC-INSTR-NR       | Parks - FSC Ice                    |
| GOLF-LABII-GF           | Parks - Golf Maint                 |
| GOLF-STAFF-ASST         | Parks - Golf Pro Shop              |
| GOLF-STFASST-GF         | Parks - Golf Pro Shop              |
| GOLF-SUP-GF             | Parks - Golf Pro Shop              |
| HEALTH WELLNESS-SPEC-GF | Parks - Health & Wellness          |
| HEALTH WELLNESS-SPEC-NR | Parks - Health & Wellness          |
| SPEC-BUS-GF             | Parks - Health & Wellness          |
| SPEC-HW-NR              | Parks - Health & Wellness          |
| CEM-LABII-GF            | Parks - Land/Cem                   |
| CEM-SPEC-GF             | Parks - Land/Cem                   |
| CEM-STAFF ASST-GF       | Parks - Land/Cem                   |
| LAND-LABII-GF           | Parks - Land/Cem                   |
| LAND-SPEC-GF            | Parks - Land/Cem                   |
| LAND-STF ASST-GF        | Parks - Land/Cem                   |
| LAND-UGSSPEC-GF         | Parks - Land/Cem                   |
| OPS-INTERN-GF           | Parks - Land/Cem                   |
| OPS-LABII-GF            | Parks - Land/Cem                   |
| UGS-LABII-GF            | Parks - Land/Cem                   |
| VEG-SPEC-GF             | Parks - Land/Cem                   |
| MP-ATT-GF               | Parks - Mills Pool                 |
| MP-GUARD-GF             | Parks - Mills Pool                 |
| MP-LEAD-NR              | Parks - Mills Pool                 |
| MP-STF AST-GF           | Parks - Mills Pool                 |
| NAT RES-SPEC-TRAILS-NR  | Parks - Natural Resource/Trail     |
| NAT-RES-EDU-SPEC-GF     | Parks - Natural Resources          |
| CE-STF ASST-NR          | Parks - Operations 1               |
| OPS-LABI-GF             | Parks - Operations 2 (Adam Street) |
| NATRES-LABII-GF         | Parks - Operations 3               |
| OPS-STF ASST-GF         | Parks - OPS Union                  |
| OPS-STF ASST-GF         | Parks - OPS/Admin                  |
| TLSP-SPEC-NR            | Parks - OPS/Admin                  |
| OPS-Lab II-GF           | Parks - OPS/Urban Forestry         |
| OPS-LABI-GF             | Parks - OPS/Urban Forestry         |
| OPS-LABII-GF            | Parks - OPS/Urban Forestry         |
| OPS-SPEC-GF             | Parks - OPS/Urban Forestry         |
| OPS-STF ASST-GF         | Parks - OPS/Urban Forestry         |
| OPS-STF-ASST-GF         | Parks - OPS/Urban Forestry         |
| OPS-STF-AST-GF          | Parks - OPS/Urban Forestry         |
| OPS-STFASST-GF          | Parks - OPS/Urban Forestry         |
| LAND-LABII-GF           | Parks - SYP Landscape              |
| LAND-SPEC-GF            | Parks - SYP Landscape              |
| LAND-SYPLABII-GF        | Parks - SYP Landscape              |
| LAND-SYPSPEC-GF         | Parks - SYP Landscape              |
| SYP-LABII-GF            | Parks - SYP Seasonal               |
| SYP-SUP-NR              | Parks - SYP Seasonal               |
| TLRC-ATT-FAC-NR         | Parks - TLRC                       |
| TLRC-ATT-FIT-NR         | Parks - TLRC                       |
| TLRC-ATT-NR             | Parks - TLRC                       |
| TLRC-STAFF ASST-NR      | Parks - TLRC                       |
| TLRC-SUP-FAC-NR         | Parks - TLRC                       |
| TLRC-SUP-NR             | Parks - TLRC                       |
| TLRC-ATT-BYB-NR         | Parks - TLRC BYB                   |
| TLRC-ATT-NR             | Parks - TLRC BYB                   |
| TLRC-BYB-ATT-NR         | Parks - TLRC BYB                   |
| TLRC-BYB-SUP-NR         | Parks - TLRC BYB                   |
| TLRC-SUP-BYB-NR         | Parks - TLRC BYB                   |
| MP-LEAD-NR              | Parks - TLRC CONC/FAC              |
| TLRC-ATT-CONC-NR        | Parks - TLRC CONC/FAC              |
| TLRC-ATT-FAC-NR         | Parks - TLRC CONC/FAC              |
| TLRC-ATT-FRT-NR         | Parks - TLRC CONC/FAC              |
| TLRC-LEAD-CONC-NR       | Parks - TLRC CONC/FAC              |
| TLRC-LEAD-FAC-NR        | Parks - TLRC CONC/FAC              |
| TLRC-SPEC-CONC-NR       | Parks - TLRC CONC/FAC              |
| TLRC-STFASST-CONC-NR    | Parks - TLRC CONC/FAC              |
| TLRC-STFASST-FAC-NR     | Parks - TLRC CONC/FAC              |
| TLRC-SUP-BLD-NR         | Parks - TLRC CONC/FAC              |
| TLRC-SUP-CONC-NR        | Parks - TLRC CONC/FAC              |
| TLRC-SUP-FAC-NR         | Parks - TLRC CONC/FAC              |
| TLRC-SUP-NR             | Parks - TLRC CONC/FAC              |
| TLSP-ATT-CONC-NR        | Parks - TLRC CONC/FAC              |
| TLSP-CONC-ATT-NR        | Parks - TLRC CONC/FAC              |
| TLRC-ATT-FRT-NR         | Parks - TLRC FRONT                 |
| TLRC-LEAD-FRT-NR        | Parks - TLRC FRONT                 |
| TLRC-LEAD-NR            | Parks - TLRC FRONT                 |
| TLRC-STFASST-FRT-NR     | Parks - TLRC FRONT                 |
| TLRC-LEAD-NR            | Parks - TLRC VOLLEY                |
| TLRC-LEAD-VOLLEY-NR     | Parks - TLRC VOLLEY                |
| TLRC-SUP-VOLLEY-NR      | Parks - TLRC VOLLEY                |
| TLRC-VOLLEY-LEAD-NR     | Parks - TLRC VOLLEY                |
| TLSP-LABII-GF           | Parks - TLSP                       |
| TLSP-SPEC-NR            | Parks - TLSP                       |
| TLSP-SUP-NR             | Parks - TLSP                       |
+-------------------------+------------------------------------+
140 

//
// all Park Jobs and Groups
//
	 select distinct g.name 'Group Name',p.name 'Job Title' from jobs j join groups g on g.id = j.group_id join positions p on p.id = j.position_id where g.department_id=5 order by 1,2

+----------------------------------------+------------------------------------+
| Job Title                              | Group Name                         |
+----------------------------------------+------------------------------------+
| Community Relations Manager            | Parks - Admin 1                    |
| Office Manager                         | Parks - Admin 1                    |
| Operations & Dev Director              | Parks - Admin 1                    |
| Recreation Services Director           | Parks - Admin 1                    |
| Sports Services Director               | Parks - Admin 1                    |
| Customer Service Rep II                | Parks - Admin 2                    |
| Customer Service Rep III               | Parks - Admin 2                    |
| Community Relations Coordinator        | Parks - Admin 3                    |
| Community Relations Specialist         | Parks - Admin 3                    |
| AJB-ATT-KC-NR                          | Parks - AJB                        |
| AJB-ATTENDANT-KC                       | Parks - AJB                        |
| AJB-LEAD KC-NR                         | Parks - AJB                        |
| AJB-LEAD-801                           | Parks - AJB                        |
| AJB-LEAD-KC-GF                         | Parks - AJB                        |
| AJB-LEAD-KC-NR                         | Parks - AJB                        |
| AJB-SPEC-KC-NR                         | Parks - AJB                        |
| AJB-ST ASST-KC-GF                      | Parks - AJB                        |
| AJB-ST ASST-KC-NR                      | Parks - AJB                        |
| AJB-STF ASST KC-NR                     | Parks - AJB                        |
| AJB-STF-ASST-NR                        | Parks - AJB                        |
| AJB-STFASST-KC-GF                      | Parks - AJB                        |
| AJB-STFASST-KC-NR                      | Parks - AJB                        |
| AJB-SUP KC-NR                          | Parks - AJB                        |
| AJB-SUP-KC                             | Parks - AJB                        |
| AJB-SUP-KC-GF                          | Parks - AJB                        |
| AJB-SUP-KC-NR                          | Parks - AJB                        |
| AJB-SUP-KC-NR 4500                     | Parks - AJB                        |
| AJB-SUP-KC-NR-4500                     | Parks - AJB                        |
| AJB-SUP-KC-NR-4501                     | Parks - AJB                        |
| AJB-SUP-NR-4500                        | Parks - AJB                        |
| BBCC-BLD-SUP-NR                        | Parks - Banneker                   |
| BBCC-LEAD-GF                           | Parks - Banneker                   |
| BBCC-LEAD-GRANT-NR                     | Parks - Banneker                   |
| BBCC-REC-LEAD-GF                       | Parks - Banneker                   |
| BBCC-SPEC-GF                           | Parks - Banneker                   |
| BBCC-SPEC-GRANT-NR                     | Parks - Banneker                   |
| BBCC-SUP-GF                            | Parks - Banneker                   |
| BBCC-SUP-NR                            | Parks - Banneker                   |
| REC-LEAD-GF                            | Parks - Banneker                   |
| BB-ATT-GF                              | Parks - Bryan Pool                 |
| BP-ATT-GF                              | Parks - Bryan Pool                 |
| BP-GUARD-GF                            | Parks - Bryan Pool                 |
| BP-LEAD-GF                             | Parks - Bryan Pool                 |
| BP-STF AST-GF                          | Parks - Bryan Pool                 |
| MP-ATT-GF                              | Parks - Bryan Pool                 |
| CE-LEAD-GF                             | Parks - Community Events           |
| CE-LEAD-NR                             | Parks - Community Events           |
| CE-SPEC-FM-NR                          | Parks - Community Events           |
| CE-SPEC-GF                             | Parks - Community Events           |
| CE-SPEC-NR                             | Parks - Community Events           |
| CE-SUP-GF                              | Parks - Community Events           |
| CE-SUP-NR                              | Parks - Community Events           |
| SPEC-HEAD TECH-GF                      | Parks - Community Events           |
| SPEC-HEAD TECH-NR                      | Parks - Community Events           |
| FSC-ATT-GF                             | Parks - FSC Front                  |
| FSC-LABII-GF                           | Parks - FSC Front                  |
| FSC-SPEC-GF                            | Parks - FSC Front                  |
| FSC-SPEC-NR                            | Parks - FSC Front                  |
| FSC-SUP-GF                             | Parks - FSC Front                  |
| FSC-HOCKEY-SPEC-NR                     | Parks - FSC Hockey                 |
| HOCKEY-SPEC-NR                         | Parks - FSC Hockey                 |
| FSC-LAB II-GF                          | Parks - FSC Ice                    |
| FSC-LABII-GF                           | Parks - FSC Ice                    |
| FSC-SPEC-INSTR-NR                      | Parks - FSC Ice                    |
| FSC-SPEC-NR                            | Parks - FSC Ice                    |
| SPEC-FSC- INSTR-NR                     | Parks - FSC Ice                    |
| SPEC-FSC-INSTR-NR                      | Parks - FSC Ice                    |
| GOLF-LABII-GF                          | Parks - Golf Maint                 |
| GOLF-STAFF-ASST                        | Parks - Golf Pro Shop              |
| GOLF-STFASST-GF                        | Parks - Golf Pro Shop              |
| GOLF-SUP-GF                            | Parks - Golf Pro Shop              |
| Golf Course Superintendent             | Parks - Golf Union                 |
| HEALTH WELLNESS-SPEC-GF                | Parks - Health & Wellness          |
| HEALTH WELLNESS-SPEC-NR                | Parks - Health & Wellness          |
| SPEC-BUS-GF                            | Parks - Health & Wellness          |
| SPEC-HW-NR                             | Parks - Health & Wellness          |
| CEM-LABII-GF                           | Parks - Land/Cem                   |
| CEM-SPEC-GF                            | Parks - Land/Cem                   |
| CEM-STAFF ASST-GF                      | Parks - Land/Cem                   |
| LAND-LABII-GF                          | Parks - Land/Cem                   |
| LAND-SPEC-GF                           | Parks - Land/Cem                   |
| LAND-STF ASST-GF                       | Parks - Land/Cem                   |
| LAND-UGSSPEC-GF                        | Parks - Land/Cem                   |
| OPS-INTERN-GF                          | Parks - Land/Cem                   |
| OPS-LABII-GF                           | Parks - Land/Cem                   |
| UGS-LABII-GF                           | Parks - Land/Cem                   |
| VEG-SPEC-GF                            | Parks - Land/Cem                   |
| Working Foreperson - Urban Green Space | Parks - Land/Cem                   |
| MP-ATT-GF                              | Parks - Mills Pool                 |
| MP-GUARD-GF                            | Parks - Mills Pool                 |
| MP-LEAD-NR                             | Parks - Mills Pool                 |
| MP-STF AST-GF                          | Parks - Mills Pool                 |
| NAT RES-SPEC-TRAILS-NR                 | Parks - Natural Resource/Trail     |
| NAT-RES-EDU-SPEC-GF                    | Parks - Natural Resources          |
| CE-STF ASST-NR                         | Parks - Operations 1               |
| Natural Resources Manager              | Parks - Operations 1               |
| Operations Superintendent              | Parks - Operations 1               |
| Urban Forester                         | Parks - Operations 1               |
| City  Landscaper                       | Parks - Operations 2 (Adam Street) |
| Operations Office Coordinator          | Parks - Operations 2 (Adam Street) |
| OPS-LABI-GF                            | Parks - Operations 2 (Adam Street) |
| NATRES-LABII-GF                        | Parks - Operations 3               |
| Natural Resources Coordinator          | Parks - Operations 3               |
| Crew Leader                            | Parks - OPS Union                  |
| Equipment Maintenance Mechanic         | Parks - OPS Union                  |
| Foreperson                             | Parks - OPS Union                  |
| Laborer                                | Parks - OPS Union                  |
| Master Motor Equipment Operator        | Parks - OPS Union                  |
| OPS-STF ASST-GF                        | Parks - OPS Union                  |
| Working Foreperson                     | Parks - OPS Union                  |
| OPS-STF ASST-GF                        | Parks - OPS/Admin                  |
| TLSP-SPEC-NR                           | Parks - OPS/Admin                  |
| Laborer                                | Parks - OPS/Urban Forestry         |
| OPS-Lab II-GF                          | Parks - OPS/Urban Forestry         |
| OPS-LABI-GF                            | Parks - OPS/Urban Forestry         |
| OPS-LABII-GF                           | Parks - OPS/Urban Forestry         |
| OPS-SPEC-GF                            | Parks - OPS/Urban Forestry         |
| OPS-STF ASST-GF                        | Parks - OPS/Urban Forestry         |
| OPS-STF-ASST-GF                        | Parks - OPS/Urban Forestry         |
| OPS-STF-AST-GF                         | Parks - OPS/Urban Forestry         |
| OPS-STFASST-GF                         | Parks - OPS/Urban Forestry         |
| Community Events Manager               | Parks - Recreation 1               |
| Coordinator/Allison Jukebox            | Parks - Recreation 1               |
| General Manager  Switchyard Park       | Parks - Recreation 1               |
| Health/Wellness Coordinator            | Parks - Recreation 1               |
| Program/Facility Coordinator           | Parks - Recreation 1               |
| Facility/Program Coordinator           | Parks - Recreation 2               |
| Program/Facility Coordinator           | Parks - Recreation 2               |
| Market Master Specialist               | Parks - Recreation 3               |
| Program Specialist                     | Parks - Recreation 4               |
| Community Events Specialist            | Parks - Recreation 5               |
| Program Specialist                     | Parks - Recreation 5               |
| General Manager TLRC                   | Parks - Sports 1                   |
| Golf Facilities Manager                | Parks - Sports 1                   |
| Sports Facility/Program Manager        | Parks - Sports 1                   |
| Aquatics/ Program Coord                | Parks - Sports 2                   |
| Membership Coordinator                 | Parks - Sports 2                   |
| Program/Facility Coordinator           | Parks - Sports 2                   |
| Sports/Facility  Coordinator           | Parks - Sports 2                   |
| Golf Course Superintendent             | Parks - Sports 3                   |
| Golf Programs Coordinator              | Parks - Sports 3                   |
| Master Motor Equipment Operator        | Parks - Sports 3                   |
| Laborer                                | Parks - Sports Union Win/FSC       |
| LAND-LABII-GF                          | Parks - SYP Landscape              |
| LAND-SPEC-GF                           | Parks - SYP Landscape              |
| LAND-SYPLABII-GF                       | Parks - SYP Landscape              |
| LAND-SYPSPEC-GF                        | Parks - SYP Landscape              |
| SYP-LABII-GF                           | Parks - SYP Seasonal               |
| SYP-SUP-NR                             | Parks - SYP Seasonal               |
| TLRC-ATT-FAC-NR                        | Parks - TLRC                       |
| TLRC-ATT-FIT-NR                        | Parks - TLRC                       |
| TLRC-ATT-NR                            | Parks - TLRC                       |
| TLRC-STAFF ASST-NR                     | Parks - TLRC                       |
| TLRC-SUP-FAC-NR                        | Parks - TLRC                       |
| TLRC-SUP-NR                            | Parks - TLRC                       |
| TLRC-ATT-BYB-NR                        | Parks - TLRC BYB                   |
| TLRC-ATT-NR                            | Parks - TLRC BYB                   |
| TLRC-BYB-ATT-NR                        | Parks - TLRC BYB                   |
| TLRC-BYB-SUP-NR                        | Parks - TLRC BYB                   |
| TLRC-SUP-BYB-NR                        | Parks - TLRC BYB                   |
| MP-LEAD-NR                             | Parks - TLRC CONC/FAC              |
| TLRC-ATT-CONC-NR                       | Parks - TLRC CONC/FAC              |
| TLRC-ATT-FAC-NR                        | Parks - TLRC CONC/FAC              |
| TLRC-ATT-FRT-NR                        | Parks - TLRC CONC/FAC              |
| TLRC-LEAD-CONC-NR                      | Parks - TLRC CONC/FAC              |
| TLRC-LEAD-FAC-NR                       | Parks - TLRC CONC/FAC              |
| TLRC-SPEC-CONC-NR                      | Parks - TLRC CONC/FAC              |
| TLRC-STFASST-CONC-NR                   | Parks - TLRC CONC/FAC              |
| TLRC-STFASST-FAC-NR                    | Parks - TLRC CONC/FAC              |
| TLRC-SUP-BLD-NR                        | Parks - TLRC CONC/FAC              |
| TLRC-SUP-CONC-NR                       | Parks - TLRC CONC/FAC              |
| TLRC-SUP-FAC-NR                        | Parks - TLRC CONC/FAC              |
| TLRC-SUP-NR                            | Parks - TLRC CONC/FAC              |
| TLSP-ATT-CONC-NR                       | Parks - TLRC CONC/FAC              |
| TLSP-CONC-ATT-NR                       | Parks - TLRC CONC/FAC              |
| TLRC-ATT-FRT-NR                        | Parks - TLRC FRONT                 |
| TLRC-LEAD-FRT-NR                       | Parks - TLRC FRONT                 |
| TLRC-LEAD-NR                           | Parks - TLRC FRONT                 |
| TLRC-STFASST-FRT-NR                    | Parks - TLRC FRONT                 |
| TLRC-LEAD-NR                           | Parks - TLRC VOLLEY                |
| TLRC-LEAD-VOLLEY-NR                    | Parks - TLRC VOLLEY                |
| TLRC-SUP-VOLLEY-NR                     | Parks - TLRC VOLLEY                |
| TLRC-VOLLEY-LEAD-NR                    | Parks - TLRC VOLLEY                |
| TLSP-LABII-GF                          | Parks - TLSP                       |
| TLSP-SPEC-NR                           | Parks - TLSP                       |
| TLSP-SUP-NR                            | Parks - TLSP                       |
| Working Foreperson                     | Parks - TLSP/SYP Union             |
+----------------------------------------+------------------------------------+
187

========================================================================
after group name change
========================================================================

 Group Name                                                   
+--------------------------------
 Parks - Allison Jukebox                             
 Parks - Banneker Community Center                   
 Parks - Bryan Pool                                  
 Parks - Cascades Golf - Pro Shop                    
 Parks - Cascades Golf Course - Maint.               
 Parks - Community Events                            
 Parks - Community Relations                         
 Parks - Customer Service                            
 Parks - Directors Managers                          
 Parks - Frank Southern Center - Front               
 Parks - Frank Southern Center - Hockey              
 Parks - Frank Southern Center - ICE                 
 Parks - Golf Report. to Facility Manager            
 Parks - Golf Union                                  
 Parks - Griffy Natural Resource                     
 Parks - Health & Wellness                           
 Parks - Landscaping and Cemeteries                  
 Parks - Mills Pool                                  
 Parks - Natural Resource Operations                 
 Parks - Natural Resource/Trail                      
 Parks - Operations Report. to OPS Director          
 Parks - Operations Report. to OPS Super.            
 Parks - OPS Union                                   
 Parks - Winslow/Olcott Parks                
 Parks - Recreation Comm. Events Coord.              
 Parks - Recreation Report. to Banneker Prog. Coord. 
 Parks - Recreation Report. to Comm. Events Manager  
 Parks - Recreation Report. to Market Coord.         
 Parks - Recreation Report. To Rec. Director         
 Parks - Rosehill Cemetery Operations                
 Parks - Skatepark                                   
 Parks - Sports Report. to Sports Director           
 Parks - Sports Union Report. to Switchyard Manager  
 Parks - SYP Landscape                               
 Parks - SYP Seasonal                                
 Parks - TLRC                                        
 Parks - TLRC Bloomington Youth Basketball           
 Parks - TLRC Concessions/Facility                   
 Parks - TLRC Front Desk                             
 Parks - TLRC Report. to Area Manager                
 Parks - TLRC VOLLEY                                 
 Parks - Twin Lakes Concessions                      
 Parks - Twin Lakes Sports Facility                  
 Parks - Twin Lakes/SYP Maintenance                  
 Parks - Urban Forestry Operations                   
------------------------------------------------------
Total: 45

select distinct g.name 'Group Name',p.name 'Job Title' from jobs j join groups g on g.id = j.group_id join positions p on p.id = j.position_id where g.department_id=5 order by 1,2

 Group Name                                          | Job Title          
-----------------------------------------------------+--------------------
Parks - Allison Jukebox                             | AJB-ATT-KC-NR
Parks - Allison Jukebox                             | AJB-ATTENDANT-KC
Parks - Allison Jukebox                             | AJB-LEAD KC-NR
Parks - Allison Jukebox                             | AJB-LEAD-801
Parks - Allison Jukebox                             | AJB-LEAD-KC-GF
Parks - Allison Jukebox                             | AJB-LEAD-KC-NR
Parks - Allison Jukebox                             | AJB-SPEC-KC-NR
Parks - Allison Jukebox                             | AJB-ST ASST-KC-GF
Parks - Allison Jukebox                             | AJB-ST ASST-KC-NR
Parks - Allison Jukebox                             | AJB-STF ASST KC-NR
Parks - Allison Jukebox                             | AJB-STF-ASST-NR
Parks - Allison Jukebox                             | AJB-STFASST-KC-GF
Parks - Allison Jukebox                             | AJB-STFASST-KC-NR
Parks - Allison Jukebox                             | AJB-SUP KC-NR
Parks - Allison Jukebox                             | AJB-SUP-KC
Parks - Allison Jukebox                             | AJB-SUP-KC-GF
Parks - Allison Jukebox                             | AJB-SUP-KC-NR
Parks - Allison Jukebox                             | AJB-SUP-KC-NR 4500
Parks - Allison Jukebox                             | AJB-SUP-KC-NR-4500
Parks - Allison Jukebox                             | AJB-SUP-KC-NR-4501
Parks - Allison Jukebox                             | AJB-SUP-NR-4500
Parks - Banneker Community Center                   | BBCC-BLD-SUP-NR
Parks - Banneker Community Center                   | BBCC-LEAD-GF
Parks - Banneker Community Center                   | BBCC-LEAD-GRANT-NR
Parks - Banneker Community Center                   | BBCC-REC-LEAD-GF
Parks - Banneker Community Center                   | BBCC-SPEC-GF
Parks - Banneker Community Center                   | BBCC-SPEC-GRANT-NR
Parks - Banneker Community Center                   | BBCC-SUP-GF
Parks - Banneker Community Center                   | BBCC-SUP-NR
Parks - Banneker Community Center                   | REC-LEAD-GF
Parks - Bryan Pool                                  | BB-ATT-GF
Parks - Bryan Pool                                  | BP-ATT-GF
Parks - Bryan Pool                                  | BP-GUARD-GF
Parks - Bryan Pool                                  | BP-LEAD-GF
Parks - Bryan Pool                                  | BP-STF AST-GF
Parks - Bryan Pool                                  | MP-ATT-GF
Parks - Cascades Golf - Pro Shop                    | GOLF-STAFF-ASST
Parks - Cascades Golf - Pro Shop                    | GOLF-STFASST-GF
Parks - Cascades Golf - Pro Shop                    | GOLF-SUP-GF
Parks - Cascades Golf Course - Maint.               | GOLF-LABII-GF
Parks - Community Events                            | CE-LEAD-GF
Parks - Community Events                            | CE-LEAD-NR
Parks - Community Events                            | CE-SPEC-FM-NR
Parks - Community Events                            | CE-SPEC-GF
Parks - Community Events                            | CE-SPEC-NR
Parks - Community Events                            | CE-SUP-GF
Parks - Community Events                            | CE-SUP-NR
Parks - Community Events                            | SPEC-HEAD TECH-GF
Parks - Community Events                            | SPEC-HEAD TECH-NR
Parks - Community Relations                         | Community Relations Coordinator     
Parks - Community Relations                         | Community Relations Specialist      
Parks - Customer Service                            | Customer Service Rep II
Parks - Customer Service                            | Customer Service Rep III
Parks - Directors Managers                          | Community Relations Manager         
Parks - Directors Managers                          | Office Manager
Parks - Directors Managers                          | Operations & Dev Director
Parks - Directors Managers                          | Recreation Services Director        
Parks - Directors Managers                          | Sports Services Director
Parks - Frank Southern Center - Front               | FSC-ATT-GF
Parks - Frank Southern Center - Front               | FSC-LABII-GF
Parks - Frank Southern Center - Front               | FSC-SPEC-GF
Parks - Frank Southern Center - Front               | FSC-SPEC-NR
Parks - Frank Southern Center - Front               | FSC-SUP-GF
Parks - Frank Southern Center - Hockey              | FSC-HOCKEY-SPEC-NR
Parks - Frank Southern Center - Hockey              | HOCKEY-SPEC-NR
Parks - Frank Southern Center - ICE                 | FSC-LAB II-GF
Parks - Frank Southern Center - ICE                 | FSC-LABII-GF
Parks - Frank Southern Center - ICE                 | FSC-SPEC-INSTR-NR
Parks - Frank Southern Center - ICE                 | FSC-SPEC-NR
Parks - Frank Southern Center - ICE                 | SPEC-FSC- INSTR-NR
Parks - Frank Southern Center - ICE                 | SPEC-FSC-INSTR-NR
Parks - Golf Report. to Facility Manager            | Golf Course Superintendent
Parks - Golf Report. to Facility Manager            | Golf Programs Coordinator
Parks - Golf Report. to Facility Manager            | Master Motor Equipment Operator     
Parks - Golf Union                                  | Golf Course Superintendent
Parks - Griffy Natural Resource                     | NAT-RES-EDU-SPEC-GF
Parks - Health & Wellness                           | HEALTH WELLNESS-SPEC-GF
Parks - Health & Wellness                           | HEALTH WELLNESS-SPEC-NR
Parks - Health & Wellness                           | SPEC-BUS-GF
Parks - Health & Wellness                           | SPEC-HW-NR
Parks - Landscaping and Cemeteries                  | CEM-LABII-GF
Parks - Landscaping and Cemeteries                  | CEM-SPEC-GF
Parks - Landscaping and Cemeteries                  | CEM-STAFF ASST-GF
Parks - Landscaping and Cemeteries                  | LAND-LABII-GF
Parks - Landscaping and Cemeteries                  | LAND-SPEC-GF
Parks - Landscaping and Cemeteries                  | LAND-STF ASST-GF
Parks - Landscaping and Cemeteries                  | LAND-UGSSPEC-GF
Parks - Landscaping and Cemeteries                  | OPS-INTERN-GF
Parks - Landscaping and Cemeteries                  | OPS-LABII-GF
Parks - Landscaping and Cemeteries                  | UGS-LABII-GF
Parks - Landscaping and Cemeteries                  | Urban Forester
Parks - Landscaping and Cemeteries                  | VEG-SPEC-GF
Parks - Landscaping and Cemeteries                  | Working Foreperson - Urban Green Space 
Parks - Mills Pool                                  | MP-ATT-GF
Parks - Mills Pool                                  | MP-GUARD-GF
Parks - Mills Pool                                  | MP-LEAD-NR
Parks - Mills Pool                                  | MP-STF AST-GF
Parks - Natural Resource Operations                 | NATRES-LABII-GF
Parks - Natural Resource Operations                 | Natural Resources Coordinator   
Parks - Natural Resource/Trail                      | NAT RES-SPEC-TRAILS-NR
Parks - Operations Report. to OPS Director          | CE-STF ASST-NR
Parks - Operations Report. to OPS Director          | Natural Resources Manager
Parks - Operations Report. to OPS Director          | Operations Superintendent
Parks - Operations Report. to OPS Director          | Urban Forester
Parks - Operations Report. to OPS Super.            | City  Landscaper
Parks - Operations Report. to OPS Super.            | Operations Office Coordinator   
Parks - Operations Report. to OPS Super.            | OPS-LABI-GF
Parks - OPS Union                                   | Crew Leader
Parks - OPS Union                                   | Equipment Maintenance Mechanic  
Parks - OPS Union                                   | Foreperson   
Parks - OPS Union                                   | Laborer      
Parks - OPS Union                                   | Master Motor Equipment Operator 
Parks - OPS Union                                   | OPS-STF ASST-GF    
Parks - OPS Union                                   | Working Foreperson
Parks - Recreation Comm. Events Coord.              | Community Events Specialist     
Parks - Recreation Comm. Events Coord.              | Program Specialist
Parks - Recreation Report. to Banneker Prog. Coord. | Program Specialist
Parks - Recreation Report. to Comm. Events Manager  | Facility/Program Coordinator    
Parks - Recreation Report. to Comm. Events Manager  | Program/Facility Coordinator    
Parks - Recreation Report. to Market Coord.         | Market Master Specialist        
Parks - Recreation Report. To Rec. Director         | Community Events Manager        
Parks - Recreation Report. To Rec. Director         | Coordinator/Allison Jukebox     
Parks - Recreation Report. To Rec. Director         | General Manager  Switchyard Park
Parks - Recreation Report. To Rec. Director         | Health/Wellness Coordinator    
Parks - Recreation Report. To Rec. Director         | Program/Facility Coordinator   
Parks - Rosehill Cemetery Operations                | OPS-STF ASST-GF        
Parks - Rosehill Cemetery Operations                | TLSP-SPEC-NR           
Parks - Sports Report. to Sports Director           | General Manager TLRC   Parks - Sports Report. to Sports Director           | Golf Facilities Manager         
Parks - Sports Report. to Sports Director           | Sports Facility/Program Manager 
Parks - Sports Union Report. to Switchyard Manager  | Laborer            
Parks - SYP Landscape                               | LAND-LABII-GF      
Parks - SYP Landscape                               | LAND-SPEC-GF       
Parks - SYP Landscape                               | LAND-SYPLABII-GF   
Parks - SYP Landscape                               | LAND-SYPSPEC-GF    
Parks - SYP Seasonal                                | SYP-LABII-GF       
Parks - SYP Seasonal                                | SYP-SUP-NR         
Parks - TLRC                                        | TLRC-ATT-FAC-NR    
Parks - TLRC                                        | TLRC-ATT-FIT-NR    
Parks - TLRC                                        | TLRC-ATT-NR        
Parks - TLRC                                        | TLRC-STAFF ASST-NR 
Parks - TLRC                                        | TLRC-SUP-FAC-NR  
Parks - TLRC                                        | TLRC-SUP-NR      
Parks - TLRC Bloomington Youth Basketball           | TLRC-ATT-BYB-NR  
Parks - TLRC Bloomington Youth Basketball           | TLRC-ATT-NR      
Parks - TLRC Bloomington Youth Basketball           | TLRC-BYB-ATT-NR  
Parks - TLRC Bloomington Youth Basketball           | TLRC-BYB-SUP-NR  
Parks - TLRC Bloomington Youth Basketball           | TLRC-SUP-BYB-NR  
Parks - TLRC Concessions/Facility                   | MP-LEAD-NR         
Parks - TLRC Concessions/Facility                   | TLRC-ATT-CONC-NR   
Parks - TLRC Concessions/Facility                   | TLRC-ATT-FAC-NR    
Parks - TLRC Concessions/Facility                   | TLRC-ATT-FRT-NR    
Parks - TLRC Concessions/Facility                   | TLRC-LEAD-CONC-NR    
Parks - TLRC Concessions/Facility                   | TLRC-LEAD-FAC-NR     
Parks - TLRC Concessions/Facility                   | TLRC-SPEC-CONC-NR    
Parks - TLRC Concessions/Facility                   | TLRC-STFASST-CONC-NR   
Parks - TLRC Concessions/Facility                   | TLRC-STFASST-FAC-NR    
Parks - TLRC Concessions/Facility                   | TLRC-SUP-BLD-NR 
Parks - TLRC Concessions/Facility                   | TLRC-SUP-CONC-NR 
Parks - TLRC Concessions/Facility                   | TLRC-SUP-FAC-NR  
Parks - TLRC Concessions/Facility                   | TLRC-SUP-NR      
Parks - TLRC Concessions/Facility                   | TLSP-ATT-CONC-NR 
Parks - TLRC Concessions/Facility                   | TLSP-CONC-ATT-NR 
Parks - TLRC Front Desk                             | TLRC-ATT-FRT-NR  
Parks - TLRC Front Desk                             | TLRC-LEAD-FRT-NR 
Parks - TLRC Front Desk                             | TLRC-LEAD-NR    
Parks - TLRC Front Desk                             | TLRC-STFASST-FRT-NR    
Parks - TLRC Report. to Area Manager                | Aquatics/ Program Coord      
Parks - TLRC Report. to Area Manager                | Membership Coordinator       
Parks - TLRC Report. to Area Manager                | Program/Facility Coordinator 
Parks - TLRC Report. to Area Manager                | Sports/Facility  Coordinator 
Parks - TLRC VOLLEY                                 | TLRC-LEAD-NR         
Parks - TLRC VOLLEY                                 | TLRC-LEAD-VOLLEY-NR    
Parks - TLRC VOLLEY                                 | TLRC-SUP-VOLLEY-NR     
Parks - TLRC VOLLEY                                 | TLRC-VOLLEY-LEAD-NR    
Parks - Twin Lakes Sports Facility                  | TLSP-LABII-GF      
Parks - Twin Lakes Sports Facility                  | TLSP-SPEC-NR       
Parks - Twin Lakes Sports Facility                  | TLSP-SUP-NR        
Parks - Twin Lakes/SYP Maintenance                  | Working Foreperson 
Parks - Urban Forestry Operations                   | Laborer         
Parks - Urban Forestry Operations                   | OPS-Lab II-GF   
Parks - Urban Forestry Operations                   | OPS-LABI-GF     
Parks - Urban Forestry Operations                   | OPS-LABII-GF    
Parks - Urban Forestry Operations                   | OPS-SPEC-GF     
Parks - Urban Forestry Operations                   | OPS-STF ASST-GF 
Parks - Urban Forestry Operations                   | OPS-STF-ASST-GF 
Parks - Urban Forestry Operations                   | OPS-STF-AST-GF  
Parks - Urban Forestry Operations                   | OPS-STFASST-GF        
+---------------------------------------------------+------------------
188 rows





		 */






}
