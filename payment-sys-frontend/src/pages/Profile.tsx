import React from 'react'
import { useUser } from '../context/UserProvider';

type Props = {}

const Profile = (props: Props) => {
  const {user} = useUser();
  return (
    <div>email: {user?.email}</div>
  )
}

export default Profile