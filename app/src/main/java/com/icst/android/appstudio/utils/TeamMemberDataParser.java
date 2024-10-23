/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio.utils;

import com.icst.android.appstudio.models.SocialProfile;
import com.icst.android.appstudio.models.TeamMember;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeamMemberDataParser {
	public static ArrayList<TeamMember> getMembers(
			String contributorsData, String contributorsAdditionalData) {
		ArrayList<TeamMember> teamMembers = new ArrayList<TeamMember>();

		try {
			JSONArray members = new JSONArray(contributorsData);
			for (int membersPosition = 0; membersPosition < members.length(); ++membersPosition) {
				JSONObject memberData = members.getJSONObject(membersPosition);
				TeamMember member = new TeamMember();
				member.setName(memberData.getString("login"));
				member.setProfilePhotoUrl(memberData.getString("avatar_url"));
				member.setDescription(memberData.getString("contributions").concat(" commits"));
				member.setId(memberData.getLong("id"));

				SocialProfile gitProfile = new SocialProfile();
				gitProfile.setPlatformName("GitHub");
				gitProfile.setPlatformIconUrl(
						"https://dl.dropbox.com/scl/fi/0xxmle6zxtg3714d04kt3/GitHub.png?rlkey=0jswbu2q9ac4s62z7icyzg5ko&dl=0");
				gitProfile.setUrl("https://github.com/".concat(memberData.getString("login")));
				ArrayList<SocialProfile> profiles = new ArrayList<SocialProfile>();
				profiles.add(gitProfile);
				member.setSocialProfiles(profiles);
				teamMembers.add(member);
			}

			/*
			 * Merge additional properties of Members
			 */
			JSONObject additionalData = new JSONObject(contributorsAdditionalData);
			JSONArray contributorsAdditionalObject = additionalData.getJSONArray("Contributors");
			for (int memberCount = 0; memberCount < contributorsAdditionalObject.length(); ++memberCount) {
				if (!contributorsAdditionalObject.getJSONObject(memberCount).isNull("id")) {

					long id = contributorsAdditionalObject.getJSONObject(memberCount).getLong("id");

					for (int teamMembersCount = 0; teamMembersCount < teamMembers.size(); ++teamMembersCount) {

						TeamMember member = teamMembers.get(teamMembersCount);
						if (member.getId() == id) {

							if (!contributorsAdditionalObject.getJSONObject(memberCount).isNull("Tag")) {
								member.setTag(
										contributorsAdditionalObject.getJSONObject(memberCount).getString("Tag"));
							}

							if (!contributorsAdditionalObject.getJSONObject(memberCount).isNull("Social")) {

								ArrayList<SocialProfile> profiles = member.getSocialProfiles();

								if (profiles == null) {
									profiles = new ArrayList<SocialProfile>();
								}

								for (int socialProfileCount = 0; socialProfileCount < contributorsAdditionalObject
										.getJSONObject(memberCount)
										.getJSONArray("Social")
										.length(); ++socialProfileCount) {
									SocialProfile profile = new SocialProfile();
									if (!contributorsAdditionalObject
											.getJSONObject(memberCount)
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.isNull("platformName")) {
										profile.setPlatformName(
												contributorsAdditionalObject
														.getJSONObject(memberCount)
														.getJSONArray("Social")
														.getJSONObject(socialProfileCount)
														.getString("platformName"));
									}
									if (!contributorsAdditionalObject
											.getJSONObject(memberCount)
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.isNull("url")) {
										profile.setUrl(
												contributorsAdditionalObject
														.getJSONObject(memberCount)
														.getJSONArray("Social")
														.getJSONObject(socialProfileCount)
														.getString("url"));
									}
									if (!contributorsAdditionalObject
											.getJSONObject(memberCount)
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.isNull("platformIconUrl")) {
										profile.setPlatformIconUrl(
												contributorsAdditionalObject
														.getJSONObject(memberCount)
														.getJSONArray("Social")
														.getJSONObject(socialProfileCount)
														.getString("platformIconUrl"));
									}
									profiles.add(profile);
								}
								member.setSocialProfiles(profiles);
							}
						}
					}
				} else {
					JSONObject nonGitContributor = contributorsAdditionalObject.getJSONObject(memberCount);
					TeamMember member = new TeamMember();
					member.setName(nonGitContributor.getString("name"));
					if (!nonGitContributor.isNull("Tag")) {
						member.setTag(nonGitContributor.getString("Tag"));
					}
					member.setProfilePhotoUrl(nonGitContributor.getString("avatar_url"));
					member.setDescription(nonGitContributor.getString("decription"));

					ArrayList<SocialProfile> profiles = new ArrayList<SocialProfile>();
					for (int socialProfileCount = 0; socialProfileCount < nonGitContributor.getJSONArray("Social")
							.length(); ++socialProfileCount) {
						SocialProfile profile = new SocialProfile();
						if (!nonGitContributor
								.getJSONArray("Social")
								.getJSONObject(socialProfileCount)
								.isNull("platformName")) {
							profile.setPlatformName(
									nonGitContributor
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.getString("platformName"));
						}
						if (!nonGitContributor
								.getJSONArray("Social")
								.getJSONObject(socialProfileCount)
								.isNull("url")) {
							profile.setUrl(
									nonGitContributor
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.getString("url"));
						}
						if (!nonGitContributor
								.getJSONArray("Social")
								.getJSONObject(socialProfileCount)
								.isNull("platformIconUrl")) {
							profile.setPlatformIconUrl(
									nonGitContributor
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.getString("platformIconUrl"));
						}
						profiles.add(profile);
					}
					member.setSocialProfiles(profiles);
					teamMembers.add(member);
				}
			}
		} catch (JSONException e) {
		}
		return teamMembers;
	}
}
